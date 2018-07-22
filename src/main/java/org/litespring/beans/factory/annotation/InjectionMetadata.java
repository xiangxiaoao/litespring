package org.litespring.beans.factory.annotation;

import java.util.List;

public class InjectionMetadata {
    //注解注入的目标类
    private final Class<?> targetClass;

    //需要注入目标类的注入元素
    private List<InjectionElement> injectionElements;

    public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElements) {
        this.targetClass = targetClass;
        this.injectionElements = injectionElements;
    }

    public List<InjectionElement> getInjectionElements() {
        return injectionElements;
    }

    public void inject(Object target) {
        if (injectionElements == null || injectionElements.isEmpty()) {
            return;
        }
        for (InjectionElement ele : injectionElements) {
            ele.inject(target);
        }
    }
}
