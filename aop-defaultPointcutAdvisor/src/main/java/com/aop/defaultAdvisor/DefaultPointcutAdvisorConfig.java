package com.aop.defaultAdvisor;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */


@Configuration
public class DefaultPointcutAdvisorConfig {


    /**
     * DefaultPointcutAdvisor 默认匹配所有类
     *
     */
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(MyMethodInterceptor myMethodInterceptor){
        DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();

        NameMatchMethodPointcut nameMatchMethodPointcut = new NameMatchMethodPointcut();
        nameMatchMethodPointcut.setMappedNames(new String[]{"execute"});
        defaultPointcutAdvisor.setPointcut(nameMatchMethodPointcut);

        defaultPointcutAdvisor.setAdvice(myMethodInterceptor);
        return defaultPointcutAdvisor;
    }

}
