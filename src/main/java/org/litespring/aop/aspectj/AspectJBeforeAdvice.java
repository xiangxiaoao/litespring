package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AspectJBeforeAdvice extends AbstractAspectJAdvice {
    public AspectJBeforeAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }


    public Object invoke(MethodInvocation mi) throws Throwable {
        //反射：调用adviceObject 类里的方法 adviceMethod
        this.invokeAdviceMethod();
        Object o = mi.proceed();
        return o;
    }
}
