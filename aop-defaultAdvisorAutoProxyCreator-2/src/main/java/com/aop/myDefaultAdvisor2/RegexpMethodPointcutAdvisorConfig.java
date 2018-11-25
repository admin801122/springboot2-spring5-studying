package com.aop.myDefaultAdvisor2;

import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */
@Configuration
public class RegexpMethodPointcutAdvisorConfig {


    /**
     * 配置正则表达式 advisor，会被 defaultAdvisorAutoProxyCreator 扫描到。最后通过通配符匹配。
     *
     */

    @Bean
    public RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor(MyMethodInterceptor myMethodInterceptor) {
        RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor = new RegexpMethodPointcutAdvisor();
        regexpMethodPointcutAdvisor.setAdvice(myMethodInterceptor);
        //匹配 com.aop.myDefaultAdvisor2.* 包中的 bean
        regexpMethodPointcutAdvisor.setPattern("com.aop.myDefaultAdvisor2.*");
        return regexpMethodPointcutAdvisor;
    }
}
