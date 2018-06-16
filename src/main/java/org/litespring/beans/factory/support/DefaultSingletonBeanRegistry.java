package org.litespring.beans.factory.support;

import org.litespring.beans.factory.config.SingletonBeanRegistry;
import org.litespring.util.Assert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();

    public void registerSingleton(String beanName, Object singleObject) {
        Assert.notNull(beanName, "beanName must not be null");
        Object oldObject = this.singletonObjects.get(beanName);
        if (oldObject != null) {
            throw new IllegalStateException("Could not register object[" + singleObject + "] under bean name '" + beanName + "':there is already object[" + oldObject + "] exist");
        }
        this.singletonObjects.put(beanName, singleObject);
    }

    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }
}
