package com.beanDefinition.registrar;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Created by zhangshukang.
 */
public class MyAutoBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName("com.beanDefinition.registrar.component.RegistrarComponent");

        registry.registerBeanDefinition("registrarComponent",beanDefinition);

    }
}
