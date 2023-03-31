package org.erym.im.distributed;

import lombok.Data;
import redis.clients.jedis.*;

import java.util.LinkedList;
import java.util.List;


/**
 * 分布式计数器
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
public class OnlineCounter
{
    private static ShardedJedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("120.46.213.254", 6379);
        jedisShardInfo1.setPassword("Dhj1314520");
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
        pool = new ShardedJedisPool(config, list);
    }


    ShardedJedis jedis = pool.getResource();
    //单例模式
    private static OnlineCounter singleInstance = null;

    private static String ONLINE_COUNT = "online_count:";


    private Long curValue;

    public static OnlineCounter getInst()
    {
        if (null == singleInstance)
        {
            singleInstance = new OnlineCounter();
            singleInstance.init();
        }
        return singleInstance;
    }

    private void init()
    {

        /**
         *  分布式计数器，失败时重试10，每次间隔30毫秒
         */
        jedis.set(ONLINE_COUNT, "0");

    }
    // 私密构造器
    private OnlineCounter()
    {

    }

    /**
     * 增加计数
     */
    public void increment()
    {
        jedis.incr(ONLINE_COUNT);
    }


    /**
     * 减少计数
     */
    public void decrement()
    {
        jedis.decr(ONLINE_COUNT);
    }


}
