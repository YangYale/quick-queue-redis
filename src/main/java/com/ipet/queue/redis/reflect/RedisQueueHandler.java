package com.ipet.queue.redis.reflect;

import com.alibaba.fastjson.JSON;
import com.ipet.queue.IHandler;
import com.ipet.queue.redis.annotation.RedisQueue;
import com.ipet.queue.redis.annotation.RedisQueueProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:22
 */
public class RedisQueueHandler implements IHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private JedisSentinelPool jedisSentinelPool;

    @Override
    public Object run(Method method, Object[] args) throws Exception{
        long rId = 0;
        if(method.getDeclaringClass().isAnnotationPresent(RedisQueue.class) && method.isAnnotationPresent(RedisQueueProvider.class)){
            if(args == null || args.length != 1){
                throw new Exception("Redis Queue Provider Java Interface Defined Error.");
            }
            RedisQueueProvider redisQueueProvider = method.getDeclaredAnnotation(RedisQueueProvider.class);
            String queueName = StringUtils.isNotBlank(redisQueueProvider.value()) ? redisQueueProvider.value() : redisQueueProvider.queueName();
            if(StringUtils.isBlank(queueName)){
                throw new Exception("Redis Queue Provider Java Config Error,please Check your configuration");
            }
            try(Jedis jedis = jedisSentinelPool.getResource()){
                switch (redisQueueProvider.strategy()){
                    case SYNC:
                        try{
                            rId = jedis.rpush(queueName, JSON.toJSONString(args[0]));
                            logger.info("Sync Send Message [{}] To Queue [{}].",JSON.toJSONString(args[0]),queueName);
                        }catch (Exception e){
                            logger.error("Sync Send Message [" + JSON.toJSONString(args[0]) + "] To Queue [" + queueName + "] Error.",e);
                        }
                        return rId;
                    case ASYNC:
                        Executors.newSingleThreadExecutor().submit(()-> {
                            try{
                                jedis.rpush(queueName,JSON.toJSONString(args[0]));
                                logger.info("Async Send Message [{}] To Queue [{}].",JSON.toJSONString(args[0]),queueName);
                            }catch (Exception e){
                                logger.error("ASync Send Message [" + JSON.toJSONString(args[0]) + "] To Queue [" + queueName + "] Error.",e);
                            }
                        });
                        return 1;
                }
            }
        }
        return rId;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,args);
        }else{
            return run(method,args);
        }
    }

    public void setJedisSentinelPool(JedisSentinelPool jedisSentinelPool) {
        this.jedisSentinelPool = jedisSentinelPool;
    }
}
