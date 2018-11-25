package com.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Created by zhangshukang on 2018/11/14.
 */
public class MyAroundAdivce implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("invoke before...");
        Object result = invocation.proceed();
        System.out.println("invoke after...");
        return result;
    }
}
