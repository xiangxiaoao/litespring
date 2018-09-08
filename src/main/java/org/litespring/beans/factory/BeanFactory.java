package org.litespring.beans.factory;

import java.util.List;

public interface BeanFactory {
    public Object getBean(String beanID);

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    List<Object> getBeansByType(Class<?> type);
}
