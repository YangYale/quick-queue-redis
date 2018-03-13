package com.ipet.queue.redis.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 14:09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RedisQueueConsumer {

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

}
