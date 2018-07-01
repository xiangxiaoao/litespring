package org.litespring.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorResolver {

    protected final Log logger = LogFactory.getLog(getClass());

    private final ConfigurableBeanFactory beanFactory;

    public ConstructorResolver(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object autowireConstructor(BeanDefinition bd) {
        Constructor<?> constructorToUse = null;
        Object[] argsToUse = null;
        Class<?> beanClass = null;

        //将RuntimeBeanReference或者TypedStringValue 转成真正的baen对象或者字符串
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(beanFactory);
        //类型转换，将字符串转为数字类型或者布尔类型
        TypeConverter typeConverter = new SimpleTypeConverter();
        //获取BeanDefinition的ConstructorArgument
        ConstructorArgument cargs = bd.getConstructorArgument();

        try {
            beanClass = this.beanFactory.getBeanClassLoader().loadClass(bd.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new BeanCreationException(bd.getID(), "Instantiation of bean faild,can't resolver class", e);
        }
        //获取bean的所有构造函数类
        Constructor<?>[] canditates = beanClass.getConstructors();
        for (int i = 0; i < canditates.length; i++) {
            //获取当前构造器的所有参数类型
            Class<?>[] parameterTypes = canditates[i].getParameterTypes();
            //构造参数个数不一致
            if (parameterTypes.length != cargs.getArgumentCount()) {
                continue;
            }
            argsToUse = new Object[parameterTypes.length];
            boolean result = this.valueMatchTypes(parameterTypes, cargs.getArgumentValues(), argsToUse, valueResolver, typeConverter);

            //说明找了要注入的类的正确的构造函数
            if (result) {
                constructorToUse = canditates[i];
                break;
            }
        }
        if(constructorToUse == null){
            throw new BeanCreationException(bd.getID(),"cant't find a apporiate constructor");
        }
        //Class.newInstance与Constructor.newInstance对比
        //1.从调用的构造函数参数来说，Class.newInstance只能调用无参构造函数，Constructor.newInstance则无此限制，原因通过Class类的getDeclaredConstructor(Class<?>... parameterTypes)方法就可以知道
        //2.从调用的构造函数的可视性来说，Class.newInstance只能调用public类型的构造函数(不能调用内部类，会抛出java.lang.ClassNotFoundException异常)，Constructor.newInstance在某些情况下可以调用private类型的构造函数
        try {
            //调用构造器类的newInstance方法创建对象
            return constructorToUse.newInstance(argsToUse);
        } catch (Exception e) {
            throw new BeanCreationException( bd.getID(), "can't find a create instance using "+constructorToUse);
        }
    }

    private boolean valueMatchTypes(Class<?>[] parameterTypes, List<ConstructorArgument.ValueHolder> valueHolders,
                                    Object[] argsToUse, BeanDefinitionValueResolver resolver, TypeConverter typeConverter) {
        for (int i = 0; i < parameterTypes.length; i++) {
            ConstructorArgument.ValueHolder valueHolder = valueHolders.get(i);
            //有可能是RuntimeBeanReference,也有可能是TypedStringValue
            Object orginaValue = valueHolder.getValue();

            try {
                //获取真正的值
                Object resolveValue = resolver.resolveValueIfNecessary(orginaValue);
                //如果参数类型是 int, 但是值是字符串,例如"3",还需要转型
                //如果转型失败，则抛出异常。说明这个构造函数不可用
                Object convertedValue = typeConverter.convertIfNecessary(resolveValue, parameterTypes[i]);
                //转型成功，记录下来
                argsToUse[i] = convertedValue;
            } catch (Exception e) {
                logger.error(e);
                return false;
            }
        }
        return true;
    }
}
