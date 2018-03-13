package com.ipet.queue;

import com.ipet.queue.redis.RedisQueueWrapper;
import com.ipet.queue.redis.annotation.RedisQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/2 14:16
 */
public enum QueueWrapperFactory {
    INSTANCE;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Map<Class<? extends Annotation>,QueueWrapper> cachedWrappers = new HashMap<>();

    QueueWrapperFactory(){
        cachedWrappers.put(RedisQueue.class,new RedisQueueWrapper());
    }

    public QueueWrapper instance(String clzName) throws Exception{
        Class clz = Class.forName(clzName);
        return instance(clz);
    }

    public QueueWrapper instance(Class<?> clz) throws Exception{
        if(clz.isAnnotationPresent(RedisQueue.class)){
            return cachedWrappers.get(RedisQueue.class);
        }else{
            logger.error("No Queue Annotation on Class [{}], please check your configuration.",clz.getName());
            throw new NullPointerException("No Queue Annotation on Class [" + clz.getName() + "], please check your configuration.");
        }
    }
}
