package org.erym.im.netty.server;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xfvape.uid.UidGenerator;
import com.xfvape.uid.impl.DefaultUidGenerator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.erym.im.common.codec.ProtobufDecoder;
import org.erym.im.common.codec.ProtobufEncoder;
import org.erym.im.common.concurrent.FutureTaskScheduler;
import org.erym.im.common.constant.ServerConstants;
import org.erym.im.common.util.IOUtil;
import org.erym.im.distributed.ImWorker;
import org.erym.im.distributed.WorkerRouter;
import org.erym.im.netty.serverHandler.ChatRedirectHandler;
import org.erym.im.netty.serverHandler.LoginRequestHandler;
import org.erym.im.netty.serverHandler.RemoteNotificationHandler;
import org.erym.im.netty.serverHandler.ServerExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import static org.erym.im.common.constant.ServerConstants.nettyName;

@Data
@Slf4j
@Service("ChatServer")
public class ChatServer {
    // 服务器端口
    @Value("${server.port}")
    private int port;
    // 通过nio方式来接收连接和处理连接
    private static final int READ_IDLE_TIME_OUT = 3600; // 读超时
    private static final int WRITE_IDLE_TIME_OUT = 0;// 写超时
    private static final int ALL_IDLE_TIME_OUT = 0; // 所有超时
    private static final String ip = "120.46.213.254"; // nacos addr
    private EventLoopGroup bg;
    private EventLoopGroup wg;

    // 启动引导器
    private ServerBootstrap b =
            new ServerBootstrap();
    @Autowired
    private LoginRequestHandler loginRequestHandler;

    @Autowired
    private ServerExceptionHandler serverExceptionHandler;

    @Autowired
    private RemoteNotificationHandler remoteNotificationHandler;

    @Autowired
    private ChatRedirectHandler chatRedirectHandler;

    private static UidGenerator uidGenerator = new DefaultUidGenerator();

    public void run() throws NacosException, UnknownHostException {
        //连接监听线程组
        bg = new NioEventLoopGroup(1);
        //传输处理线程组
        wg = new NioEventLoopGroup();
        //1 设置reactor 线程
        b.group(bg, wg);
        //2 设置nio类型的channel
        b.channel(NioServerSocketChannel.class);
        //3 设置监听端口
        String ip = IOUtil.getHostAddress();
        b.localAddress(new InetSocketAddress(ip, port));
        //4 设置通道选项
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        //5 装配流水线
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达时会创建一个channel
            protected void initChannel(SocketChannel ch) throws Exception {
                //此事件被HeartbeatHandler的userEventTriggered方法处理到
                ch.pipeline().addLast("deCoder", new ProtobufDecoder());
                ch.pipeline().addLast("enCoder", new ProtobufEncoder());
                // 在流水线中添加handler来处理登录,登录后删除
                ch.pipeline().addLast("login", loginRequestHandler);
//                ch.pipeline().addLast("heartBeat", new HeartBeatServerHandler());
                ch.pipeline().addLast("remoteNotificationHandler", remoteNotificationHandler);
                ch.pipeline().addLast("chatRedirect", chatRedirectHandler);
                //当连接在60秒内没有接收到消息时，就会触发一个IdleStateEvent事件，
                ch.pipeline().addLast(new IdleStateHandler(READ_IDLE_TIME_OUT, WRITE_IDLE_TIME_OUT, ALL_IDLE_TIME_OUT, TimeUnit.SECONDS));
                ch.pipeline().addLast("serverException", serverExceptionHandler);
            }
        });
        // 6 开始绑定server
        // 通过调用sync同步方法阻塞直到绑定成功

        ChannelFuture channelFuture = null;
        boolean isStart = false;
        while (!isStart) {
            try {

                channelFuture = b.bind().sync();
                log.info("疯狂创客圈 CrazyIM 启动, 端口为： " +
                        channelFuture.channel().localAddress());
                isStart = true;
            } catch (Exception e) {
                log.error("发生启动异常", e);
                port++;
                log.info("尝试一个新的端口：" + port);
                b.localAddress(new InetSocketAddress(port));
            }
        }

        Instance instance = new Instance();
        instance.addMetadata("balance", "0");
        instance.setPort(port);
        instance.setIp(ip);

        NamingService namingService = NamingFactory.createNamingService(ServerConstants.nacosServer);
        InetAddress address = InetAddress.getLocalHost();
        namingService.registerInstance(nettyName, instance);

        channelFuture.channel().closeFuture().syncUninterruptibly();

        ImWorker.getInst().setLocalNode(ip, port);

        FutureTaskScheduler.add(() ->
        {
            /**
             * 分布式节点管理
             */
            ImWorker.getInst().init();

            WorkerRouter.getInst().init();

        });



        //JVM关闭时的钩子函数
        Runtime.getRuntime().addShutdownHook(
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // 8 优雅关闭EventLoopGroup，
                        // 释放掉所有资源包括创建的线程
                        wg.shutdownGracefully();
                        bg.shutdownGracefully();
                    }
                }));
        try {
            // 7 监听通道关闭事件
            // 应用程序会一直等待，直到channel关闭
            ChannelFuture closeFuture =
                    channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (
                Exception e) {
            log.error("发生其他异常", e);
        } finally {
            // 8 优雅关闭EventLoopGroup，
            // 释放掉所有资源包括创建的线程
            wg.shutdownGracefully();
            bg.shutdownGracefully();
        }

    }

}
