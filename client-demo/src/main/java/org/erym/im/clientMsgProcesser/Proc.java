package org.erym.im.clientMsgProcesser;


import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.client.ClientSession;

/**
 * 操作类
 */
public interface Proc
{

    ProtoMsg.HeadType op();

    void action(ClientSession ch, ProtoMsg.Message proto) throws Exception;

}
