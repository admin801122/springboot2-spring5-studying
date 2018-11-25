package com.aop.beanNameAutoProxyCreator;

import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */
@Configuration
public class NameMatchMethodPointcutAdvisorConfig {

    @Bean
    public NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor(MyMethodInterceptor myMethodInterceptor){

        NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor = new NameMatchMethodPointcutAdvisor();
        //匹配方法名称为 execute 的方法
        nameMatchMethodPointcutAdvisor.setMappedName("execute");
        //设置通知
        nameMatchMethodPointcutAdvisor.setAdvice(myMethodInterceptor);
        return nameMatchMethodPointcutAdvisor;
    }
}
