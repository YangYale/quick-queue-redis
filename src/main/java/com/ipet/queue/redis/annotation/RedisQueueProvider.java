package com.ipet.queue.redis.annotation;

import com.ipet.queue.enums.SendStrategyEn;

import java.lang.annotation.*;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 13:55
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisQueueProvider {

    /**
     * 队列名称 和queueName二选一
     * @return
     */
    String value() default "";

    /**
     * 队列名称 和value二选一
     * @return
     */
    String queueName() default "";

    /**
     * 发送策略，默认同步
     * @return
     */
    SendStrategyEn strategy() default SendStrategyEn.SYNC;
}
