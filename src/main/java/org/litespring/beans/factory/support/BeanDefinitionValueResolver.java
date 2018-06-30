package org.litespring.beans.factory.support;

import org.litespring.beans.factory.BeanFactory;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;

/**
 * 实现在sertter注入或者构造器注入时，根据ref 或者 value 属性得到真正的bean 或字符串
 */
public class BeanDefinitionValueResolver {
    private final BeanFactory beanFactory;

    public BeanDefinitionValueResolver(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = beanFactory.getBean(refName);
            return bean;
        } else if(value instanceof TypedStringValue){
             return((TypedStringValue) value).getValue();
        }else {
            //TODO
            throw new RuntimeException("the value " + value + " has not implemented");
        }
    }
}
