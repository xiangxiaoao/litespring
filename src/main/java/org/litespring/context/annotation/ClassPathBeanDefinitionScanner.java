package org.litespring.context.annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.BeanNameGenerator;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner {
    private final BeanDefinitionRegistry registry;

    private PackageResourceLoader resourceLoader = new PackageResourceLoader();

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    protected final Log logger = LogFactory.getLog(ClassPathBeanDefinitionScanner.class);

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public Set<BeanDefinition> doScan(String packagesToScan) {
        String[] basePackages = StringUtils.tokenizeToStringArray(packagesToScan, ",");
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<BeanDefinition>();
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                beanDefinitions.add(candidate);
                registry.registerBeanDefinition(candidate.getID(), candidate);
            }
        }
        return beanDefinitions;
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
        try {
            Resource[] resources = this.resourceLoader.getResources(basePackage);
            for (Resource resource : resources) {
                MetadataReader metadataReader = new SimpleMetadataReader(resource);
                if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                    ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                    String beanName = this.beanNameGenerator.generateBeanName(sbd, this.registry);
                    sbd.setId(beanName);
                    candidates.add(sbd);
                }
            }
        } catch (IOException e) {
            logger.error("I/O failure during classpath scanning");
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }
        return candidates;
    }
}
