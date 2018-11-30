package com.aop.regexpMethodPointcutAdvisor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * Created by zhangshukang
 */

@Component
public class MyRegexpInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(2);
        return invocation.proceed();
    }
}
