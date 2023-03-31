package org.erym.im.netty.serverProcesser;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.erym.im.common.constant.MsgConstant;
import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.common.model.vo.MessageVo;
import org.erym.im.common.util.SpringUtil;
import org.erym.im.netty.server.session.LocalSession;
import org.erym.im.netty.server.session.ServerSession;
import org.erym.im.netty.server.session.service.SessionManger;
import org.erym.im.service.MsgService;
import org.erym.im.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service("ChatRedirectProcesser")
public class ChatRedirectProcesser extends AbstractServerProcesser {

    public static final int RE_DIRECT = 1;

    @Override
    public ProtoMsg.HeadType op() {
        return ProtoMsg.HeadType.MESSAGE_REQUEST;
    }
    @Autowired
    MsgService msgService;

    @Override
    public Boolean action(LocalSession fromSession, ProtoMsg.Message proto) {
        // 聊天处理
        ProtoMsg.MessageRequest messageRequest = proto.getMessageRequest();
        log.info(("chatMsg | from="
                + messageRequest.getFrom()
                + " , to =" + messageRequest.getTo()
                + " , MsgType =" + messageRequest.getMsgType()
                + " , content =" + messageRequest.getContent()));

        // 获取接收方的chatID
        String to = messageRequest.getTo();
//        RedisService redisService = SpringUtil.getBean(RedisService.class);
//        if (redisService.isBan(Integer.valueOf(to))) return false; //禁言
        log.info("[" + to + "] 在线，发送消息");
        // int platform = messageRequest.getPlatform();
        List<ServerSession> toSessions = SessionManger.inst().getSessionsBy(to);

        // 消息异步入库
        if (toSessions == null) {
            //接收方离线
            log.info("[" + to + "] 不在线，需要保存为离线消息，请保存到nosql如mongo中!");
        } else {

            toSessions.forEach((session) ->
            {
                // 将IM消息发送到接收客户端；
                // 如果是remoteSession，则转发到对应的服务节点
                session.writeAndFlush(proto);
                msgService.sendMessage(proto);

            });
        }
        return null;
    }

}
