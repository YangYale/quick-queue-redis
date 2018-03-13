package com.ipet.queue.redis.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 16:32
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RedisQueueScannerRegistrar.class)
public @interface RedisQueueScanner {
    /**
     * 扫描包的位置
     * @return
     */
    String[] value() default {};

    /**
     * 扫描包的位置，与value的值叠加
     * @return
     */
    String[] basePackages() default {};
}
