/**
 * Created by 尼恩 at 疯狂创客圈
 */

package org.erym.im.protoBuilder;

import org.erym.im.common.model.dto.UserDTO;
import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.client.ClientSession;


/**
 * 心跳消息Builder
 */
public class HeartBeatMsgBuilder extends BaseBuilder
{
    private final UserDTO user;

    public HeartBeatMsgBuilder(UserDTO user, ClientSession session)
    {
        super(ProtoMsg.HeadType.HEART_BEAT, session);
        this.user = user;
    }

    public ProtoMsg.Message buildMsg()
    {
        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.MessageHeartBeat.Builder lb =
                ProtoMsg.MessageHeartBeat.newBuilder()
                        .setSeq(0)
                        .setJson("{\"from\":\"client\"}")
                        .setUid(String.valueOf(user.getUserId()));
        return message.toBuilder().setHeartBeat(lb).build();
    }


}


