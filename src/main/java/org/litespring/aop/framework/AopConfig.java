package org.litespring.aop.framework;

import org.litespring.aop.Advice;

import java.lang.reflect.Method;
import java.util.List;

public interface AopConfig {
    Class<?> getTargetClass();

    Object getTargetObject();

    boolean isProxyTargetClass();

    // this is for JDK interface proxy
    Class<?>[] getProxiedInterfaces();

    // this is for JDK interface proxy
    boolean isInterfaceProxied(Class<?> intf);

    List<Advice> getAdvices();

    void addAdvice(Advice advice);

    List<Advice> getAdvices(Method method/*, Class<?> targetClass*/);

    void setTargetObject(Object obj);
}
