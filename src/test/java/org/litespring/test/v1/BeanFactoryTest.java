package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.service.v1.PetStoreService;
import org.litespring.service.v1.PrototypeService;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

/**
 * bean 测试类
 */
public class BeanFactoryTest {
    DefaultBeanFactory factory = null;
    XmlBeanDefinitionReader reader = null;

    @Before
    public void setUP() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }

    @Test
    public void testGetBean() throws IOException {
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
        BeanDefinition bd = factory.getBeanDefinition("petStore");
        assertTrue(bd.isSingleton());
        assertFalse(bd.isPrototype());
        assertEquals(BeanDefinition.SCOPE_DEFAULT, bd.getScope());
        assertEquals("org.litespring.service.v1.PetStoreService", bd.getBeanClassName());
        PetStoreService petStore = (PetStoreService) factory.getBean("petStore");
        assertNotNull(petStore);

        //验证是否是单例
        PetStoreService petStore1 = (PetStoreService) factory.getBean("petStore");

        assertTrue(petStore.equals(petStore1));
    }

    @Test
    public void testInvalidBean() {
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
        try {
            factory.getBean("invalidBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("expext BeanCreationException");
    }

    @Test
    public void testInvalidXML() {
        try {
            reader.loadBeanDefinitions(new ClassPathResource("xxx.xml"));
        } catch (BeanDefinitionStoreException e) {
            return;
        }
        Assert.fail("expect BeanDefinitionStoreEception");
    }

    @Test
    public  void testPrototypeBean(){
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v1.xml"));
        BeanDefinition bd = factory.getBeanDefinition("prototypeBean");
        assertTrue(bd.isPrototype());
        assertFalse(bd.isSingleton());
        PrototypeService service = (PrototypeService)factory.getBean("prototypeBean");
        PrototypeService service1 = (PrototypeService)factory.getBean("prototypeBean");
        assertFalse(service.equals(service1));
    }

}
