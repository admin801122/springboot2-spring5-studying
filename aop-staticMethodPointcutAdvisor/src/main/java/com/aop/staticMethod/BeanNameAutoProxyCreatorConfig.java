package com.aop.staticMethod;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */

@Configuration
public class BeanNameAutoProxyCreatorConfig {

    @Bean
    @ConditionalOnClass(MyStaticMethodPointcutAdvisor.class)
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator(){

        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        //设置 advisor，可以理解为设置通知
        beanNameAutoProxyCreator.setInterceptorNames("myStaticMethodPointcutAdvisor");
        return beanNameAutoProxyCreator;

    }

}
