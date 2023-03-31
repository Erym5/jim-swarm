package org.erym.im.netty.server.session;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
public interface ServerSession
{
    void writeAndFlush(Object pkg);

    String getSessionId();

    boolean isValid();

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    Long getUserId();
}

