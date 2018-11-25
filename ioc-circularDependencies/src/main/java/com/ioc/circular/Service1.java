package com.ioc.circular;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by zhangshukang.
 */

@Service
public class Service1 implements InitializingBean {

    @Autowired
    Service2 service2;

    public Service1(){
        System.out.println("service1 init start...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("service1 init end...");
    }
}
