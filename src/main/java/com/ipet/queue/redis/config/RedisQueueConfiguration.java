package com.ipet.queue.redis.config;

import com.ipet.queue.HandlerFactory;
import com.ipet.queue.QueueWrapper;
import com.ipet.queue.redis.annotation.RedisQueue;
import com.ipet.queue.redis.reflect.RedisQueueHandler;
import com.ipet.queue.redis.RedisQueueWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:38
 */
@Configuration
public class RedisQueueConfiguration {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnClass(JedisSentinelPool.class)
    public JedisSentinelPool jedisSentinelPool() throws Exception{
        try{
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(200);
            jedisPoolConfig.setMaxIdle(-1);
            JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(redisProperties.getSentinel().getMaster(),
                    new HashSet<>(Arrays.asList(redisProperties.getSentinel().getNodes().split(","))),jedisPoolConfig,
                    0,redisProperties.getPassword(),redisProperties.getDatabase());
            HandlerFactory.INSTANCE.modifyHandlerProperty(RedisQueue.class,"jedisSentinelPool",jedisSentinelPool);
            logger.info(">>>>>>>>>>>>>>>>>>>>>> Initial Jedis Pool Successfully.");
            return jedisSentinelPool;
        }catch (Exception e){
            logger.error(">>>>>>>>>>>>>>>>>>>>>> Initial Jedis Pool Error.",e);
            throw e;
        }
    }
}
