package com.aop.beanNameAutoProxyCreator;

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

        System.out.println("myBeanNameInterceptor before invoke...");

        Object result= invocation.proceed();

        System.out.println("myBeanNameInterceptor after invoke...");
        return result;
    }
}
