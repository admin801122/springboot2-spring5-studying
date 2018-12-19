package com.beanPostProcessor.allBeanPostProcessor;

import com.beanPostProcessor.component.MyComponent;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.stream.Stream;

/**
 * Created by zhangshukang on 2018/12/19.
 */

@Component
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {


    private static final String SPECIAL_PROPERTY_NAME = "creator";


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        if (beanClass.getName().equals(MyComponent.class.getName())) {
            System.out.println("myComponent postProcessBeforeInstantiation...");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {

        if (bean.getClass().getName().equals(MyComponent.class.getName())) {
            System.out.println("myComponent postProcessBeforeInstantiation...");
        }

        return true;
    }

    //属性注入方法，@Autowired，@Resource 等注解原理基于此方法实现
    //手动对 MyComponent对象creator字段添加值 system
    @Override
    public PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        if (pvs instanceof MutablePropertyValues && bean.getClass().getName().equals(MyComponent.class.getName())) {

            System.out.println("myComponent postProcessPropertyValues...");

            Stream.of(pds).forEach(p -> {

                if (SPECIAL_PROPERTY_NAME.equals(p.getName())) {
                    ((MutablePropertyValues) pvs).getPropertyValueList().add(new PropertyValue(SPECIAL_PROPERTY_NAME, "system"));
                }

            });
        }
        return pvs;
    }

}
