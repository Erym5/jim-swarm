package org.erym.im.service;

import org.erym.im.distributed.OnlineCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author cjt
 * @date 2022/6/12 11:09
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${netty.connector-url}")
    public String connectorUrl;

    private static final String ONLINE_USER = "online_user";
    private static final String BAN_USER = "ban_user:";

    public void online(Integer userId) {
        redisTemplate.opsForSet().add(ONLINE_USER, String.valueOf(userId));
        OnlineCounter.getInst().increment();
    }

    public void offline(Integer userId) {
        redisTemplate.opsForSet().remove(ONLINE_USER, String.valueOf(userId));
        OnlineCounter.getInst().decrement();
    }


    public boolean isBan(Integer userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BAN_USER + userId));
    }
}
