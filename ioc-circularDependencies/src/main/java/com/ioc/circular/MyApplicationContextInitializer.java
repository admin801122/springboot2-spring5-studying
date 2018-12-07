package com.ioc.circular;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by zhangshukang.
 */
public class MyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {


        applicationContext.getBeanFactory().addBeanPostProcessor(new BeanPostProcessor() { 
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        });

        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                return bean;
            }
        });


        applicationContext.getBeanFactory().addBeanPostProcessor(new SmartInstantiationAwareBeanPostProcessor() {
            @Override
            public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
                return null;
            }

            public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
                return bean;
            }
        });


    }
}
