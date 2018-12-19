package com.beanPostProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by zhangshukang on 2018/12/19.
 */

@Component
public class MyBeanPostProcessor  implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(null == bean)
            return null;

        if (bean.getClass().getName().equals(MyComponent.class.getName())) {
            System.out.println("component postProcessBeforeInitialization...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(null == bean)
            return null;

        if (bean.getClass().getName().equals(MyComponent.class.getName())) {
            System.out.println("component postProcessAfterInitialization...");
        }
        return bean;
    }

}
