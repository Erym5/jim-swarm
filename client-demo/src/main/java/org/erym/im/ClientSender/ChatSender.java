package org.erym.im.ClientSender;

import org.erym.im.common.model.protocol.ChatMsg;
import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.clientBuilder.ChatMsgBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("ChatSender")
public class ChatSender extends BaseSender
{
    public void sendChatMsg(String touid, String content)
    {
        ChatMsg chatMsg = new ChatMsg(getUser());
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setTo(touid);
        chatMsg.setMsgId(System.currentTimeMillis());
        ProtoMsg.Message message =
                ChatMsgBuilder.buildChatMsg(chatMsg, getUser(), getSession());
//        commandClient.waitCommandThread();
        super.sendMsg(message);
    }

    @Override
    protected void sendSucced(ProtoMsg.Message message)
    {


        log.info("单聊发送成功:"
                + message.getMessageRequest().getContent()
                + "->"
                + message.getMessageRequest().getTo());
//        commandClient.notifyCommandThread();
    }

    @Override
    protected void sendException(ProtoMsg.Message message)
    {
        log.info("单聊发送异常:"
                + message.getMessageRequest().getContent()
                + "->"
                + message.getMessageRequest().getTo());
//        commandClient.notifyCommandThread();
    }

    @Override
    protected void sendfailed(ProtoMsg.Message message)
    {
        log.info("单聊发送失败:"
                + message.getMessageRequest().getContent()
                + "->"
                + message.getMessageRequest().getTo());
//        commandClient.notifyCommandThread();
    }
}
