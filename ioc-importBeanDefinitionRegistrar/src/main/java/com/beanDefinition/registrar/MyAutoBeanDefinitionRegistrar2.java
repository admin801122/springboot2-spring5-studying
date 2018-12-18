package com.beanDefinition.registrar;

import com.beanDefinition.registrar.component.RegistrarComponent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.awt.*;
import java.util.Set;

/**
 * Created by zhangshukang.
 */
public class MyAutoBeanDefinitionRegistrar2 implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
        scan.addIncludeFilter(new AssignableTypeFilter(RegistrarComponent.class));
        Set<BeanDefinition> candidateComponents = scan.findCandidateComponents("com.beanDefinition.registrar.component");

        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

        candidateComponents.stream().forEach(beanDefinition->{

            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            if (!registry.containsBeanDefinition(beanDefinition.getBeanClassName())) {
                registry.registerBeanDefinition(beanName,beanDefinition);
            }
        });
    }
}
