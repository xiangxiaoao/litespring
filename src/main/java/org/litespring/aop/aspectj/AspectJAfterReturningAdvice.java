package org.litespring.aop.aspectj;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

public class AspectJAfterReturningAdvice extends AbstractAspectJAdvice {


    public AspectJAfterReturningAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        super(adviceMethod, pointcut, adviceObject);
    }


    public Object invoke(MethodInvocation mi) throws Throwable {
        Object o = mi.proceed();
        this.invokeAdviceMethod();
        return o;
    }
}
