package com.aop.staticMethod;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;

import java.lang.reflect.Method;

/**
 * Created by zhangshukang
 */



public class MyStaticMethodPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /*
     * 匹配所有的 execute 方法
     */
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return "execute".equals(method.getName());
    }


    /**
     *
     * 默认匹配所有的类。有些底层框架类不能被重写，另外匹配所有的类效率比较低。
     * 使用时记得根据需求重写，缩小匹配范围。
     * 这里匹配 MyRealClass及其子类。
     *
     */
    @Override
    public ClassFilter getClassFilter() {
        return (clazz)-> MyRealClass.class.isAssignableFrom(clazz);
    }
}
