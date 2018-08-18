package org.litespring.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectiveMethodInvocation implements MethodInvocation {
    protected final Object targetObject;//petStoreService

    protected final Method tartgetMethod;//petStoreService.placeOrder()

    protected Object[] arguments;//方法的参数数组

    /**
     * List of MethodInterceptor
     */
    protected final List<MethodInterceptor> interceptors;

    /**
     * Index from 0 of the current interceptor we're invoking.
     * -1 until we invoke:then the current interceptor.
     */
    private int currentInterceptorIndex = -1;

    public ReflectiveMethodInvocation(Object targetObject, Method tartgetMethod, Object[] arguments, List<MethodInterceptor> interceptors) {
        this.targetObject = targetObject;
        this.tartgetMethod = tartgetMethod;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }

    public Method getMethod() {
        return this.tartgetMethod;
    }

    public Object[] getArguments() {
        return (this.arguments != null ? this.arguments : new Object[0]);
    }

    public Object getThis() {
        return this.targetObject;
    }

    public AccessibleObject getStaticPart() {
        return this.tartgetMethod;
    }

    public Object proceed() throws Throwable {
        //所有拦截器调用完毕
        if (this.currentInterceptorIndex == this.interceptors.size() - 1) {
            return invokeJoinpoint();
        }
        this.currentInterceptorIndex++;
        MethodInterceptor interceptor = this.interceptors.get(currentInterceptorIndex);
        return interceptor.invoke(this);
    }

    /**
     * Invoke the joinpoint using reflection.
     * Subclasses can override this to use custom invocation.
     *
     * @return the return value of the joinpoint
     * @throws Throwable if invoking the joinpoint resulted in an exception
     */
    protected Object invokeJoinpoint() throws Throwable {
        return this.tartgetMethod.invoke(targetObject, arguments);
    }

}
