package com.aop;

import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import sun.misc.Launcher;

import java.lang.reflect.Method;

/**
 * Created by zhangshukang on 2018/10/23.
 */
public class MyMethodInterceptor implements MethodInterceptor {

    public static void main(String[] args) {
        System.out.println(Launcher.class.getClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader());


        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\code");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealService.class);
        enhancer.setUseCache(true);
        enhancer.setCallback(new MyMethodInterceptor());

        RealService realService = (RealService) enhancer.create();
        realService.realMethod();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before exexute");
        Object result=methodProxy.invokeSuper(obj, objects);
        System.out.println("after exexute");
        return result;
    }
}
