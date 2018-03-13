package com.ipet.queue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * 队列Interface代理的实现工厂
 * @author 杨斌冰-工具组-技术中心
 *         <p>
 *         2018/3/2 14:09
 */
public class QueueFactoryBean<T> implements FactoryBean<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String interfaceClassName;

    @Override
    public T getObject() throws Exception {
        Class clz = Class.forName(interfaceClassName);
        if(clz.isInterface()){
            QueueWrapper queueWrapper = QueueWrapperFactory.INSTANCE.instance(clz);
            return queueWrapper.getInstance((Class<T>) clz);
        }else{
            throw new Exception("The Class [" + this.interfaceClassName + "] is not a interface, please check your code.");
        }
    }

    @Override
    public Class<?> getObjectType() {
        try {
            if(StringUtils.isBlank(this.interfaceClassName)){
                return null;
            }
            return Class.forName(this.interfaceClassName);
        } catch (ClassNotFoundException e) {
            logger.error("Class [" + this.interfaceClassName + "] is not found.",e);
            return null;
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }
}
