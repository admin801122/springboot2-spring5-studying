package com.ioc.circular;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by zhangshukang.
 */
@Service
public class Service2 implements InitializingBean {

    @Autowired Service1 service1;

    public Service2(){
        System.out.println("service2 init start...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("service2 init end...");

    }
}
