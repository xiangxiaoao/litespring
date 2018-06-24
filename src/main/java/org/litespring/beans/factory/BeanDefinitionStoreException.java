package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

/**
 * bean解析异常类
 */
public class BeanDefinitionStoreException extends BeansException {

    public BeanDefinitionStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
