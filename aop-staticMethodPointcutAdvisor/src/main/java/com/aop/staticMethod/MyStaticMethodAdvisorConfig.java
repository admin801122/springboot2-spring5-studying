package com.aop.staticMethod;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */
@Configuration
public class MyStaticMethodAdvisorConfig {

    @Bean(value = "myStaticMethodPointcutAdvisor")
    public MyStaticMethodPointcutAdvisor myStaticPointcutAdvisor(MyMethodInterceptor myMethodInterceptor) {
        MyStaticMethodPointcutAdvisor myStaticMethodPointcutAdvisor = new MyStaticMethodPointcutAdvisor();
        myStaticMethodPointcutAdvisor.setAdvice(myMethodInterceptor);
        return myStaticMethodPointcutAdvisor;
    }
}
