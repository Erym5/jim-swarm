package org.erym.im.netty.serverProcesser;


import org.erym.im.common.model.protocol.ProtoMsg;
import org.erym.im.netty.server.session.LocalSession;

/**
 * 操作类
 */
public interface ServerProcesser
{

    ProtoMsg.HeadType type();

    boolean action(LocalSession ch, ProtoMsg.Message proto);

}
