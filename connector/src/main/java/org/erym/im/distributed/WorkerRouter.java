package org.erym.im.distributed;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.erym.im.common.concurrent.FutureTaskScheduler;
import org.erym.im.common.constant.ServerConstants;
import org.erym.im.common.model.entity.ImNode;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
@Slf4j
public class WorkerRouter {

    private ImNode node = null;


    private static WorkerRouter singleInstance = null;
    @NacosInjected
    private NamingService naming;



    private ConcurrentHashMap<Long, PeerSender> workerMap =
            new ConcurrentHashMap<>();


    public synchronized static WorkerRouter getInst() {
        if (null == singleInstance) {
            singleInstance = new WorkerRouter();
        }
        return singleInstance;
    }

    private WorkerRouter() {

    }

    private boolean inited=false;

    /**
     * 初始化节点管理
     */
    public void init() {

        if(inited)
        {
            return;
        }
        inited=true;

        try {
            if (null == naming) {
                try {
                    this.naming = NamingFactory.createNamingService(ServerConstants.nacosServer);
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }

            }
            try {
                naming.subscribe(ServerConstants.nettyName, event -> {
                    if (event instanceof NamingEvent) {
                        List<Instance> instances = ((NamingEvent) event).getInstances();
                        for (Map.Entry<Long, PeerSender> entry : workerMap.entrySet()) {
                            entry.getValue().setOnline(false);
                        }

                        for (Instance instance : instances) {
                            Long id = Long.valueOf(instance.getInstanceId());
                            if (workerMap.containsKey(id)) {
                                workerMap.get(instance.getInstanceId()).setOnline(true);
                            } else {
                                ImNode rmNode = new ImNode(instance.getIp(), instance.getPort());
                                PeerSender peerSender = new PeerSender(rmNode);
                                peerSender.setOnline(true);
                                FutureTaskScheduler.add(() ->
            {
                                peerSender.doConnect();
                            });
                                workerMap.put(id, peerSender);
                            }
                        }
                        for (Map.Entry<Long, PeerSender> entry : workerMap.entrySet()) {
                            if (entry.getValue().getConnectFlag() == false) {
                                entry.getValue().stopConnecting();
                                workerMap.remove(entry.getKey());
                            }
                        }
                    }
                });
            } catch (NacosException e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    //订阅节点的增加和删除事件
    public PeerSender route(Long id) {
        PeerSender peerSender = workerMap.get(id);
        if (null != peerSender) {
            return peerSender;
        }
        return null;
    }


    public void sendNotification(String json) {
        workerMap.keySet().stream().forEach(
                key ->
                {
                    if (!key.equals(getLocalNode().getId())) {
                        PeerSender peerSender = workerMap.get(key);
                        peerSender.writeAndFlush(json);
                    }
                }
        );

    }


    public ImNode getLocalNode() {
        return ImWorker.getInst().getLocalNodeInfo();
    }
}
