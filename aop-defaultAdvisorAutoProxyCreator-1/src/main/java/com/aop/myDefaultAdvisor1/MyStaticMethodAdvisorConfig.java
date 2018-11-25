package com.aop.myDefaultAdvisor1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */
@Configuration
public class MyStaticMethodAdvisorConfig {

    @Bean
    public MyStaticMethodPointcutAdvisor myStaticPointcutAdvisor(MyMethodInterceptor myMethodInterceptor) {
        MyStaticMethodPointcutAdvisor myStaticMethodPointcutAdvisor = new MyStaticMethodPointcutAdvisor();
        myStaticMethodPointcutAdvisor.setAdvice(myMethodInterceptor);
        return myStaticMethodPointcutAdvisor;
    }
}
