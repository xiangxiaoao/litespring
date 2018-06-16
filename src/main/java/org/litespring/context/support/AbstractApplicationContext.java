package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

/**
 * 经典设计模式：模板方法
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    private DefaultBeanFactory factory = null;

    private ClassLoader beanClassLoader;

    public AbstractApplicationContext(String configFile) {
        factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = getResourcePath(configFile);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.beanClassLoader);
    }

    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    protected abstract Resource getResourcePath(String path);
}
