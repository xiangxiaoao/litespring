package org.litespring.test.v3;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationTestV3.class,
        BeanDefinitionTestV3.class,
        ConstructorResolverTest.class
})
public class V3AllTests {
}
