package org.litespring.core.type.classreading;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleMetadataReader implements MetadataReader {

    private final Resource resource;

    private final ClassMetadata classMetadata;

    private final AnnotationMetadata annotationMetadata;

    public SimpleMetadataReader(Resource resource) throws IOException {
        InputStream is = new BufferedInputStream(resource.getInputStream());
        ClassReader classReader = null;
        try {
            classReader = new ClassReader(is);
        } catch (IOException e) {
            is.close();
        }
        AnnotationMetadataReadingVistor vistor = new AnnotationMetadataReadingVistor();
        classReader.accept(vistor, ClassReader.SKIP_DEBUG);
        this.annotationMetadata = vistor;
        this.classMetadata = vistor;
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }

    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }
}
