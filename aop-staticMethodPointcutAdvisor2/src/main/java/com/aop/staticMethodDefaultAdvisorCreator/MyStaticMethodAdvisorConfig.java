package com.aop.staticMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */
@Configuration
public class MyStaticMethodAdvisorConfig {

    @Bean(value = "myStaticMethodPointcutAdvisor")
    public com.aop.staticMethod.MyStaticMethodPointcutAdvisor myStaticPointcutAdvisor(com.aop.staticMethod.MyMethodInterceptor myMethodInterceptor) {
        com.aop.staticMethod.MyStaticMethodPointcutAdvisor myStaticMethodPointcutAdvisor = new com.aop.staticMethod.MyStaticMethodPointcutAdvisor();
        myStaticMethodPointcutAdvisor.setAdvice(myMethodInterceptor);
        return myStaticMethodPointcutAdvisor;
    }
}
