package org.erym.im.distributed;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xfvape.uid.UidGenerator;
import com.xfvape.uid.impl.DefaultUidGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.erym.im.common.constant.ServerConstants;
import org.erym.im.common.model.entity.ImNode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;


/**
 * IM 节点的Nacos协调客户端
 **/
@Data
@Slf4j
public class ImWorker {

    private ImNode localNode = null;

    private static ImWorker singleInstance = null;
    private boolean inited = false;
    @NacosInjected
    NamingService namingService;

    // IP
    String IP;

    {
        try {
            IP = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    //取得单例
    public synchronized static ImWorker getInst() {

        if (null == singleInstance) {

            singleInstance = new ImWorker();
            singleInstance.localNode = new ImNode();
        }
        return singleInstance;
    }

    private ImWorker() {

    }

    public synchronized void init() {

        if (inited) {
            return;
        }
        inited = true;
        if (null == localNode) {
            localNode = new ImNode();
        }


        try {
            //为node 设置id
            UidGenerator uidGenerator = new DefaultUidGenerator();
            localNode.setId(uidGenerator.getUID());
            //获取nacos服务
            setNamingService();
            /*实例数据*/
            Instance instance = new Instance();
            instance.setInstanceId(String.valueOf(uidGenerator.getUID()));
            instance.setIp(IP);
            instance.setPort(ServerConstants.PORT);
            instance.addMetadata("balance", localNode.getBalance().toString());
            //将服务注册到注册中心
            namingService.registerInstance(ServerConstants.nettyName, instance);

            log.info("本地节点, path={}, id={}", localNode.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLocalNode(String ip, int port) {
        localNode.setHost(ip);
        localNode.setPort(port);
    }


    /**
     * 增加负载，表示有用户登录成功
     *
     * @return 成功状态
     */
    public boolean incBalance() {
        if (null == localNode) {
            throw new RuntimeException("还没有设置Node 节点");
        }
        // 增加负载：增加负载，并写回zookeeper
        while (true) {
            try {
                localNode.incrementBalance();
                List<Instance> instances = namingService.selectInstances(ServerConstants.nettyName, true);
                for (Instance instance : instances) {
                    if (instance.getIp().equals(IP) && instance.getPort() == ServerConstants.PORT) {
                        instance.addMetadata("balance", localNode.getBalance().toString());
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

    }

    private NamingService setNamingService() throws NacosException {
        this.namingService = NamingFactory.createNamingService(ServerConstants.nacosServer);
        return namingService;
    }

    /**
     * 减少负载，表示有用户下线，写回zookeeper
     *
     * @return 成功状态
     */
    public boolean decrBalance() {
        if (null == localNode) {
            throw new RuntimeException("还没有设置Node 节点");
        }
        while (true) {
            try {

                localNode.decrementBalance();

                List<Instance> instances = namingService.selectInstances(ServerConstants.nettyName, true);
                for (Instance instance : instances) {
                    if (instance.getIp().equals(IP) && instance.getPort() == ServerConstants.PORT) {
                        instance.addMetadata("balance", localNode.getBalance().toString());
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }

    }


    /**
     * 返回本地的节点信息
     *
     * @return 本地的节点信息
     */
    public ImNode getLocalNodeInfo() {
        return localNode;
    }

    // 注销服务实例
    private void deleteNode() {
        log.info("删除 worker node, id={}", localNode.getId());
        try {
            namingService.deregisterInstance(ServerConstants.nettyName, IP, ServerConstants.PORT);
        } catch (NacosException ex) {
            throw new RuntimeException(ex);
        }
    }

    static {
        //JVM关闭时的钩子函数
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    ImWorker.getInst().deleteNode();
                }, "关掉worker，注销实例"));
    }

}