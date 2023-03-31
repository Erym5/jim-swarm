package org.erym.im.common.model.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.erym.im.common.model.dto.UserDTO;

import java.io.Serializable;

/**
 * @author cjt
 * @date 2022/5/4 22:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg{
//    private Integer senderId; //发送者id
//    private Integer receiverId; //接收者(用户/群组)id
//    private String message; //消息内容
//    private Integer type; //类型(1:群聊, 2:私聊)

    //消息类型  1：纯文本  2：音频 3：视频 4：地理位置 5：其他
    public enum MSGTYPE
    {
        TEXT,
        AUDIO,
        VIDEO,
        POS,
        OTHER;
    }

    public ChatMsg(UserDTO user)
    {
        if (null == user)
        {
            return;
        }
        this.user = user;
        this.setTime(System.currentTimeMillis());
        this.setFrom(String.valueOf(user.getUserId()));

    }

    private UserDTO user;

    private long msgId;
    private String from;
    private String to;
    private long time;
    private MSGTYPE msgType;
    private String content;
    private String url;          //多媒体地址
    private String property;     //附加属性
    private String fromNick;     //发送者昵称
    private String json;         //附加的json串


    public void fillMsg(ProtoMsg.MessageRequest.Builder cb)
    {
        if (msgId > 0)
        {
            cb.setMsgId(msgId);
        }
        if (StringUtils.isNotEmpty(from))
        {
            cb.setFrom(from);
        }
        if (StringUtils.isNotEmpty(to))
        {
            cb.setTo(to);
        }
        if (time > 0)
        {
            cb.setTime(time);
        }
        if (msgType != null)
        {
            cb.setMsgType(msgType.ordinal());
        }
        if (StringUtils.isNotEmpty(content))
        {
            cb.setContent(content);
        }
        if (StringUtils.isNotEmpty(url))
        {
            cb.setUrl(url);
        }
        if (StringUtils.isNotEmpty(property))
        {
            cb.setProperty(property);
        }
        if (StringUtils.isNotEmpty(fromNick))
        {
            cb.setFromNick(fromNick);
        }

        if (StringUtils.isNotEmpty(json))
        {
            cb.setJson(json);
        }
    }
}
