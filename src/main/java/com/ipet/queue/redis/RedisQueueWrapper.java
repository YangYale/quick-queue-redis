package com.ipet.queue.redis;

import com.ipet.queue.HandlerFactory;
import com.ipet.queue.QueueWrapper;

import java.lang.reflect.Proxy;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:19
 */
public class RedisQueueWrapper implements QueueWrapper{

    @Override
    public <T> T getInstance(Class<T> clz) throws Exception {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(),new Class[]{clz}, HandlerFactory.INSTANCE.instance(clz));
    }


}
