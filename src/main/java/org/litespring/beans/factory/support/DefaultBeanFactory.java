package org.litespring.beans.factory.support;


import org.apache.commons.beanutils.BeanUtils;
import org.litespring.beans.*;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanFactoryAware;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.config.BeanPostProcessor;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.litespring.util.ClassUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {


    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    private ClassLoader beanClassLoader;

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    public DefaultBeanFactory() {
    }

    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    public void registerBeanDefinition(String beanID, BeanDefinition bd) {
        this.beanDefinitionMap.put(beanID, bd);
    }

    public Object getBean(String beanID) {
        BeanDefinition bd = this.getBeanDefinition(beanID);
        if (bd == null) {
            throw new BeanCreationException("Bean Definition doea not exist");
        }
        if (bd.isSingleton()) {
            Object bean = this.getSingleton(beanID);
            if (bean == null) {
                bean = createBean(bd);
                this.registerSingleton(beanID, bean);
            }
            return bean;
        }
        return createBean(bd);
    }

    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.getBeanDefinition(name);
        if (bd == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(bd);
        return bd.getBeanClass();
    }

    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<Object>();
        List<String> beanIDs = this.getBeanIDsByType(type);
        for (String beanID : beanIDs) {
            result.add(this.getBean(beanID));
        }
        return result;
    }

    private List<String> getBeanIDsByType(Class<?> type) {
        List<String> result = new ArrayList<String>();
        for (String beanName : this.beanDefinitionMap.keySet()) {
            if (type.isAssignableFrom(this.getType(beanName))) {
                result.add(beanName);
            }
        }
        return result;
    }


    protected Object createBean(BeanDefinition bd) {

        //创建实例 instantiate：实例化
        Object bean = instantiateBean(bd);

        //设置属性 populate:填充
        populateBean(bd, bean);

        bean = initializeBean(bd,bean);
        //用Commons BeanUtils工具包来填充
        //populateBeanUseCommonBeanUtils(bd,bean);

        return bean;
    }

    private Object instantiateBean(BeanDefinition bd) {
        //判断BeanDefinition是否有构造器注入
        if (bd.hasConstructorArgumentValues()) {
            ConstructorResolver resolver = new ConstructorResolver(this);
            return resolver.autowireConstructor(bd);
        } else {
            ClassLoader cl = this.getBeanClassLoader();
            String beanClassName = bd.getBeanClassName();
            try {
                Class<?> clz = cl.loadClass(beanClassName);
                return clz.newInstance();
            } catch (Exception e) {
                throw new BeanCreationException("Create bean for" + beanClassName + " failed", e);
            }
        }
    }

    private void populateBean(BeanDefinition bd, Object bean) {
        //Autowired 注解注入
        for (BeanPostProcessor postProcessor : this.getBeanPostProcessors()) {
            if (postProcessor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) postProcessor).postProcessPropertyValues(bean, bd.getID());
            }
        }
        List<PropertyValue> pvs = bd.getPropertyValues();
        if (pvs == null || pvs.isEmpty()) {
            return;
        }
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        //SimpleTypeConverter converter = new SimpleTypeConverter();
        TypeConverter converter = new SimpleTypeConverter();
        try {
            for (PropertyValue pv : pvs) {
                String propertyName = pv.getName();
                // RuntimeBeanReference 对象 或者  TypedStringValue 对象
                Object originalValue = pv.getValue();
                //转化为真正的对象实例
                Object resolveValue = resolver.resolveValueIfNecessary(originalValue);

                //获取bean的信息，然后获取bean的所有属性，然后将resolveValue 设置set进去
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                //PropertyDescriptor （属性描述器）类表示 JavaBean 类通过存储器导出一个属性
                PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor pd : pds) {
                    if (pd.getName().equals(propertyName)) {
                        Object convertedValue = converter.convertIfNecessary(resolveValue, pd.getPropertyType());
                        pd.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class[" + bd.getBeanClassName() + "]", e);
        }
    }

    //用Commons BeanUtils工具包来实现类型转换与注入
    private void populateBeanUseCommonBeanUtils(BeanDefinition bd, Object bean) {
        List<PropertyValue> pvs = bd.getPropertyValues();
        if (pvs == null || pvs.isEmpty()) {
            return;
        }
        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(this);
        try {
            for (PropertyValue pv : pvs) {
                String propertyName = pv.getName();
                // RuntimeBeanReference 对象 或者  TypedStringValue 对象
                Object originalValue = pv.getValue();
                //转化为真正的对象实例
                Object resolveValue = resolver.resolveValueIfNecessary(originalValue);
                //实现注入
                BeanUtils.setProperty(bean, propertyName, resolveValue);
            }
        } catch (Exception e) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class[" + bd.getBeanClassName() + "]", e);
        }

    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return (this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader());
    }

    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        this.beanPostProcessors.add(postProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDependencyType();
        for (BeanDefinition bd : this.beanDefinitionMap.values()) {
            //确保BeanDefinition 有Class对象
            resolveBeanClass(bd);
            Class<?> beanClass = bd.getBeanClass();
            //1)isAssignableFrom方法判断A是否是B的父类或者和B类型相同或者B实现了A接口.是类与类之间的比较
            //2)instanceOf方法判断A是否为B的实例，其中A为对象，B为类型.用来判断一个对象实例是否是一个类或接口的或其子类子接口的实例
            if (typeToMatch.isAssignableFrom(beanClass)) {
                return this.getBean(bd.getID());
            }
        }
        return null;
    }

    public void resolveBeanClass(BeanDefinition bd) {
        if (bd.hasBeanClass()) {
            return;
        } else {
            try {
                //调用ClassLoader 加载类( loadClass.)
                bd.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:" + bd.getBeanClassName());
            }
        }
    }

    protected Object initializeBean(BeanDefinition bd, Object bean) {
        invokeAwareMehtods(bean);
        //Todo,对Bean做初始化
        //创建代理
        if(!bd.isSynthetic()){
             return applyBeanPostProcessorsAfterInitialization(bean,bd.getID());
        }
        return bean;
    }

   public Object applyBeanPostProcessorsAfterInitialization(Object existingBean,String beanName) throws BeansException {
         Object result = existingBean;
         for(BeanPostProcessor beanProcessor:getBeanPostProcessors()){
             result = beanProcessor.afterInitialization(result,beanName);
             if(result == null){
                 return result;
             }
         }
         return result;
   }

    private void invokeAwareMehtods(final Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }
}
