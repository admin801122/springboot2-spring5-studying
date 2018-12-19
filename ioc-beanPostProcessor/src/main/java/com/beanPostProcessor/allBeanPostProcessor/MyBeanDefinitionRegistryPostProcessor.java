package com.beanPostProcessor.allBeanPostProcessor;

import com.beanPostProcessor.component.MyBeanDefinitionRegistrar;
import com.beanPostProcessor.component.MySingleton;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by zhangshukang.
 */

@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    //注册 beanDefinition
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registry.registerBeanDefinition("myBeanDefinitionRegistrar",new AnnotatedGenericBeanDefinition(MyBeanDefinitionRegistrar.class));
    }

    //注册 singleton
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerSingleton("mySingleton", new MySingleton());
    }
}
