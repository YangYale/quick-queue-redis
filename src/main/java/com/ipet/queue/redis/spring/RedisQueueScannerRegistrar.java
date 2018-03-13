package com.ipet.queue.redis.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 16:32
 */
public class RedisQueueScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RedisQueueScanner.class.getName()));
        ClassPathRedisQueueScanner classPathRedisQueueScanner = new ClassPathRedisQueueScanner(registry);
        if(this.resourceLoader != null){
            classPathRedisQueueScanner.setResourceLoader(this.resourceLoader);
        }

        List<String> packages = new ArrayList<>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                packages.add(pkg);
            }
        }

        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                packages.add(pkg);
            }
        }
        classPathRedisQueueScanner.doScan(StringUtils.toStringArray(packages));
    }


}
