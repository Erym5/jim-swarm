package org.erym.im.common.constant;

import io.netty.util.AttributeKey;

public class ServerConstants {
    public static final AttributeKey<String> CHANNEL_NAME =
            AttributeKey.valueOf("CHANNEL_NAME");


    public static final String nacosServer = "http://localhost:8848";
    public static final String nettyName = "netty-service";
    public static final int PORT = 9000;

    public static final String WEB_URL = "http://localhost:8010";



}
