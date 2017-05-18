package com.cielo.ex2.remote;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by 63289 on 2017/5/17.
 */
public enum RedisUtil {
    REDIS_UTIL;
    private static JedisPool jedisPool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(128);
        config.setMaxTotal(1024);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);
        jedisPool = new JedisPool(config, "127.0.0.1", 6379, 1000, "hahaschool");
    }
    public Jedis getJedis() {
        return jedisPool.getResource();
    }
}
