/**
 * Created by 尼恩 at 疯狂创客圈
 */

package org.erym.im.clientBuilder;

import org.erym.im.common.model.dto.UserDTO;
import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.client.ClientSession;


/**
 * 登陆消息Builder
 */
public class LoginMsgBuilder extends BaseBuilder
{
    private final UserDTO user;

    public LoginMsgBuilder(
            UserDTO user,
            ClientSession session)
    {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, session);
        this.user = user;
    }

    public ProtoMsg.Message build()
    {
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.LoginRequest.Builder lb =
                ProtoMsg.LoginRequest.newBuilder()
                        .setDeviceId(user.getClientId())
                        .setToken(user.getToken())
                        .setUid(String.valueOf(user.getUserId()));
        return message.toBuilder().setLoginRequest(lb).build();
    }

    public static ProtoMsg.Message buildLoginMsg(
            UserDTO user,
            ClientSession session)
    {
        LoginMsgBuilder builder = new LoginMsgBuilder(user, session);
        return builder.build();

    }
}


