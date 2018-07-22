package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.classreading.AnnotationMetadataReadingVistor;
import org.litespring.core.type.classreading.ClassMetadataReadingVistor;
import org.springframework.asm.ClassReader;

import java.io.IOException;

public class ClassReaderTest {
    @Test
    public void testGetClasMetaData() throws IOException {
        ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());

        ClassMetadataReadingVistor vistor = new ClassMetadataReadingVistor();

        reader.accept(vistor, ClassReader.SKIP_CODE);

        Assert.assertFalse(vistor.isAbstract());
        Assert.assertFalse(vistor.isInterface());
        Assert.assertEquals("org.litespring.service.v4.PetStoreService", vistor.getClassName());
        Assert.assertEquals(0, vistor.getInterfaces().length);

    }

    @Test
    public void testGetAnnonation() throws Exception {
        ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());

        AnnotationMetadataReadingVistor vistor = new AnnotationMetadataReadingVistor();

        reader.accept(vistor, ClassReader.SKIP_DEBUG);

        String annotation = "org.litespring.stereotype.Component";
        Assert.assertTrue(vistor.hasAnnotation(annotation));

        AnnotationAttributes attributes = vistor.getAnnotationAttributes(annotation);

        Assert.assertEquals("petStore", attributes.get("value"));
    }
}
