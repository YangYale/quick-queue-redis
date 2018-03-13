package com.ipet.queue.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ipet.queue.IQueueConsumer;
import com.ipet.queue.redis.annotation.RedisQueueConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 15:31
 */
public abstract class AbstractRedisQueueConsumer<T> implements IQueueConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JedisSentinelPool jedisSentinelPool;

    private String queueName;

    public abstract void doConsume(T message);

    @Override
    public void consume() throws Exception{
        if(this.getClass().isAnnotationPresent(RedisQueueConsumer.class)){
            RedisQueueConsumer redisQueueConsumer = this.getClass().getAnnotation(RedisQueueConsumer.class);
            queueName = StringUtils.isNotBlank(redisQueueConsumer.value()) ? redisQueueConsumer.value() : redisQueueConsumer.queueName();
            if(StringUtils.isBlank(queueName)){
                throw new Exception("Redis Queue Consumer Java Config Error.");
            }
            try(Jedis jedis = jedisSentinelPool.getResource()){
                while (true){
                    List<String> message = jedis.blpop(0,queueName);
                    if(message != null && message.size() > 1){
                        this.doConsume(JSON.parseObject(message.get(1),new TypeReference<T>(){}));
                    }
                }
            }
        }else{
            throw new Exception("Redis Queue Consumer Java Config Error.");
        }
    }

    @PostConstruct
    public void init(){
        logger.info("The Consumer in Queue [{}] Started.",this.getClass().getName());
        Executors.newSingleThreadExecutor().submit(()->{
            try {
                this.consume();
            } catch (Exception e) {
                logger.error("The Consumer in Queue [" + this.queueName + "] Error.",e);
            }
        });
    }
}
