package com.aop;

import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by zhangshukang on 2018/10/28.
 */
public class MyInvokeHandler implements InvocationHandler {

    RealService realService;


    public MyInvokeHandler(RealService realService) {
        this.realService = realService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before exexute");
        Object result = method.invoke(realService, args);
        System.out.println("after exexute");
        return result;
    }


    public static void main(String[] args) {

        //性能测试
        Long num = 100000000l;
        jdkProxyTest(num);
        cglibProxyTest(num);
        System.out.println("-------------------------");

    }

        public static void jdkProxyTest(Long num){

            RealService realService = new RealService();
            MyInvokeHandler myInvokeHandler = new MyInvokeHandler(realService);

            IRealService iRealService = (IRealService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{IRealService.class}, myInvokeHandler);

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < num; i++) {
                iRealService.realMethod();
            }
            System.out.println("jdk proxy 执行"+num+"次，耗时："+(System.currentTimeMillis()-startTime));
        }


    public static void cglibProxyTest(Long num){

//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\code");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealService.class);
        enhancer.setUseCache(true);
        enhancer.setCallback(new MyMethodInterceptor());
        RealService realService = (RealService) enhancer.create();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            realService.realMethod();
        }
        System.out.println("cglib proxy 执行"+num+"次，耗时："+(System.currentTimeMillis()-startTime));
    }

}
