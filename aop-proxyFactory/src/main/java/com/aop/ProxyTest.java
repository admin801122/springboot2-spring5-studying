package com.aop;

import org.springframework.aop.framework.ProxyFactory;

/**
 * Created by zhangshukang on 2018/11/7.
 */
public class ProxyTest {

    public static void main(String[] args) {

        MyService myService = new MyServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(myService);

        proxyFactory.addAdvice(new MyBeforeAdvice());

        proxyFactory.addAdvice(new MyAroundAdivce());

        proxyFactory.addAdvice((new MyAfterReturnAdivce()));

        MyService proxyBean = (MyService) proxyFactory.getProxy();
        proxyBean.execute();
    }
}
