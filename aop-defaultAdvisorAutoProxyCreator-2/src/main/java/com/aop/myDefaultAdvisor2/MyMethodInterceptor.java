package com.aop.myDefaultAdvisor2;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

/**
 * Created by zhangshukang
 */

@Component
public class MyMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        System.out.println("myStaticMethodInterceptor before invoke...");

        Object result= invocation.proceed();

        System.out.println("myStaticMethodInterceptor after invoke...");
        return result;
    }
}
