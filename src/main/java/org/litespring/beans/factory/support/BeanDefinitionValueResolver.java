package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.BeansException;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.FactoryBean;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * 实现在sertter注入或者构造器注入时，根据ref 或者 value 属性得到真正的bean 或字符串
 */
public class BeanDefinitionValueResolver {
    private final AbstractBeanFactory beanFactory;

    public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = beanFactory.getBean(refName);
            return bean;
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else if (value instanceof BeanDefinition) {
            //Resolve plain BeanDefinition without contained name:use dummy name.
            BeanDefinition bd = (BeanDefinition) value;
            String innerBeanName = "(inner bean)" + bd.getBeanClassName() + "#" + Integer.toHexString(System.identityHashCode(bd));
            return resolveInnerBean(innerBeanName, bd);
        } else {
            return value;
        }
    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBd) {
        try {
            Object innerBean = this.beanFactory.createBean(innerBd);
            if (innerBean instanceof FactoryBean) {
                try {
                    return ((FactoryBean<?>) innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName, "FactoryBean threw exception on object creation", e);
                }
            } else {
                return innerBean;
            }
        } catch (BeansException ex) {
            throw new BeanCreationException(
                    innerBeanName,
                    "Cannot create inner bean '" + innerBeanName + "' " +
                            (innerBd != null && innerBd.getBeanClassName() != null ? "of type [" + innerBd.getBeanClassName() + "] " : "")
                    , ex);
        }
    }
}
