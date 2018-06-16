package org.litespring.beans.factory.config;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singleObject);

    Object getSingleton(String beanName);
}
