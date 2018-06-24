package org.litespring.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationTestV2.class,
        BeanDefinitionoValueResolverTest.class,
        BeanDefinitionTestV2.class,
        CustomBooleanEditorTest.class,
        CustomNumberEditorTest.class,
        TypeConverterTest.class
})
public class V2AllTests {
}
