package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

/**
 * 获取beand的classLoader
 */
public interface ConfigurableBeanFactory extends BeanFactory {
    void setBeanClassLoader(ClassLoader beanClassLoader);
    ClassLoader getBeanClassLoader();
}
