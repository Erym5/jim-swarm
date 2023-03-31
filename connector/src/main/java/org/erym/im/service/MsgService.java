package org.erym.im.service;

import org.erym.im.common.constant.MsgConstant;
import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.common.util.DateUtil;
import org.erym.im.feign.ChatService;
import org.erym.im.feign.UserInfoService;
import org.erym.im.common.model.entity.GroupMessage;
import org.erym.im.common.model.entity.Message;
import org.erym.im.common.model.entity.User;
import org.erym.im.common.model.vo.MessageVo;
import org.erym.im.netty.server.session.ServerSession;
import org.erym.im.netty.server.session.service.SessionManger;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author cjt
 * @date 2022/6/11 23:57
 */
@Slf4j
@Component
public class MsgService {
    @Autowired
    private UserInfoService userInfoService;
    @Resource
    private ChatService chatService;
    /**
     * 消息异步入库
     */
    public void sendMessage(ProtoMsg.Message pkg) {
        ProtoMsg.MessageRequest request = pkg.getMessageRequest();
        Long senderId = Long.valueOf(request.getFrom());
        Long receiverId = Long.valueOf(request.getTo());
        String msg = request.getContent();

        User user = userInfoService.findUserById(senderId).getData();
        Message message = new Message();
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setContent(msg);
        message.setGmtCreate(DateUtil.getDate());
        //封装页面展示对象
        MessageVo messageVo = new MessageVo();
        messageVo.setId(message.getId());
        messageVo.setSender(message.getSender());
        messageVo.setReceiver(message.getReceiver());
        messageVo.setContent(message.getContent());
        messageVo.setGmtCreate(message.getGmtCreate());
        messageVo.setUserName(user.getUserName());
        messageVo.setAvatar(user.getAvatar());

        chatService.saveMessage(message);
    }
}
