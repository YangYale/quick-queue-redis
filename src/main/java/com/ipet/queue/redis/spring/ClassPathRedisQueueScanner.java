package com.ipet.queue.redis.spring;

import com.ipet.queue.QueueFactoryBean;
import com.ipet.queue.redis.annotation.RedisQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/1 16:42
 */
public class ClassPathRedisQueueScanner extends ClassPathBeanDefinitionScanner{
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ClassPathRedisQueueScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public ClassPathRedisQueueScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public ClassPathRedisQueueScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public ClassPathRedisQueueScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if(beanDefinitions.isEmpty()){
            logger.warn("No Redis Queues was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        }else{
            this.processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitionHolders){
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder beanDefinitionHolder: beanDefinitionHolders) {
            definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            //注入interface时确保注入的是代理
            definition.getPropertyValues().add("interfaceClassName",definition.getBeanClassName());
            //通过factorybean来生成代理对象
            definition.setBeanClass(QueueFactoryBean.class);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) || beanDefinition.getMetadata().hasAnnotation(RedisQueue.class.getName());
    }

    @Override
    protected void registerDefaultFilters() {
        //添加需要取的注解类
        this.addIncludeFilter(new AnnotationTypeFilter(RedisQueue.class));
    }
}
