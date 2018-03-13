package com.ipet.queue;

import com.ipet.queue.redis.annotation.RedisQueue;
import com.ipet.queue.redis.reflect.RedisQueueHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/2 14:45
 */
public enum HandlerFactory {
    INSTANCE;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<Class<? extends Annotation>,IHandler> cachedHandlers = new HashMap<>();

    HandlerFactory(){
        cachedHandlers.put(RedisQueue.class,new RedisQueueHandler());
    }

    public IHandler instance(String clzName) throws Exception{
        Class clz = Class.forName(clzName);
        return instance(clz);
    }

    public IHandler instance(Class<?> clz) throws Exception{
        if(clz.isAnnotationPresent(RedisQueue.class)){
            return cachedHandlers.get(RedisQueue.class);
        }else{
            logger.error("No Queue Annotation on Class [{}], please check your configuration.",clz.getName());
            throw new NullPointerException("No Queue Annotation on Class [" + clz.getName() + "], please check your configuration.");
        }
    }

    /**
     * 动态添加handler的参数
     * @param key
     * @param propertyName
     * @param value
     * @throws Exception
     */
    public void modifyHandlerProperty(Class<? extends Annotation> key,String propertyName,Object value) throws Exception{
        IHandler handler = cachedHandlers.get(key);
        Field field = handler.getClass().getDeclaredField(propertyName);
        ReflectionUtils.setField(field,handler,value);
        cachedHandlers.put(key,handler);
    }
}
