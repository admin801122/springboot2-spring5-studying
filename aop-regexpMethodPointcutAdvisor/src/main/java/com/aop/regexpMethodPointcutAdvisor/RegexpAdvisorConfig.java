package com.aop.regexpMethodPointcutAdvisor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */
@Configuration
public class RegexpAdvisorConfig {

    @Bean
    public MyRegexpMethodPointcutAdvisor myRegexpMethodPointcutAdvisor(MyRegexpInterceptor myRegexpInterceptor) {
        MyRegexpMethodPointcutAdvisor myRegexpMethodPointcutAdvisor = new MyRegexpMethodPointcutAdvisor();
        myRegexpMethodPointcutAdvisor.setAdvice(myRegexpInterceptor);
        myRegexpMethodPointcutAdvisor.setPattern("com.aop.regexpMethodPointcutAdvisor.*");
        return myRegexpMethodPointcutAdvisor;
    }
}
