package com.beanPostProcessor.component;

/**
 * Created by zhangshukang.
 */

//手动注册 beanDefinition
public class MyBeanDefinitionRegistrar {

    static {

        System.out.println("beanDefinitionRegistrar init...");
    }

}
