package org.litespring.aop.aspectj;

import org.litespring.aop.Advice;
import org.litespring.aop.Pointcut;

import java.lang.reflect.Method;

public abstract class AbstractAspectJAdvice  implements Advice {

    private Method adviceMethod;
    private AspectJExpressionPointcut pointcut;
    private Object adviceObject;

    public AbstractAspectJAdvice(Method adviceMethod, AspectJExpressionPointcut pointcut, Object adviceObject) {
        this.adviceMethod = adviceMethod;
        this.pointcut = pointcut;
        this.adviceObject = adviceObject;
    }

    public void invokeAdviceMethod() throws Throwable {
        adviceMethod.invoke(adviceObject);
    }

    public Method getAdviceMethod() {
        return this.adviceMethod;
    }

    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
