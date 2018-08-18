package org.litespring.aop.framework;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;
import org.litespring.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class AopConfigSupport implements AopConfig {
    private boolean proxyTargetClass = false;
    private Object targetObject = null;
    private List<Advice> advices = new ArrayList<Advice>();
    private List<Class> interfaces = new ArrayList<Class>();

    public AopConfigSupport() {
    }

    public Object getTargetObject() {
        return this.targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    public void addInterface(Class<?> intf) {
        Assert.notNull(intf, "Interface must not be null");
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);

        }
    }

    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    public boolean isProxyTargetClass() {
        return this.proxyTargetClass;
    }

    public List<Advice> getAdvices() {
        return this.advices;
    }

    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }

    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<Advice>();
        for (Advice advice : this.advices) {
            Pointcut pc = advice.getPointcut();
            if (pc.getMethodMatcher().matches(method)) {
                result.add(advice);
            }
        }
        return result;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }
}
