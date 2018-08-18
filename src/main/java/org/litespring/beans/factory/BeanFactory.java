package org.litespring.beans.factory;

public interface BeanFactory {
    public Object getBean(String beanID);

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;
}
