package org.erym.im;

import com.alibaba.nacos.api.exception.NacosException;
import org.erym.im.netty.server.ChatServer;
import org.erym.im.netty.server.session.service.SessionManger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import java.net.UnknownHostException;

@SpringBootApplication
@EnableFeignClients
public class ServerApplication
{

    /**
     * @param args
     */
    public static void main(String[] args) throws UnknownHostException, NacosException {
        // 启动并初始化 Spring 环境及其各 Spring 组件
        ApplicationContext context =
                SpringApplication.run(ServerApplication.class, args);
        /**
         * 将SessionManger 单例设置为spring bean
         */
        SessionManger sessionManger = context.getBean(SessionManger.class);
        SessionManger.setSingleInstance(sessionManger);

        /**
         * 启动服务
         */
        ChatServer nettyServer = context.getBean(ChatServer.class);
        nettyServer.run();


    }

}