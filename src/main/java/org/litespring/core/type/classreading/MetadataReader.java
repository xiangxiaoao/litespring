package org.litespring.core.type.classreading;

import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;

/**
 * Simple facade for accessing class metadata,
 * as read by an ASM {@link org.springframework.asm.ClassReader}
 */
public interface MetadataReader {

    /**
     * Return the resource for the class file.
     *
     * @return
     */
    Resource getResource();

    /**
     * Read basic metadata for the underlying class.
     *
     * @return
     */
    ClassMetadata getClassMetadata();

    /**
     * Read full annotation metadata for the underlying class,
     * including metadata for annotated methods.
     *
     * @return
     */
    AnnotationMetadata getAnnotationMetadata();
}
