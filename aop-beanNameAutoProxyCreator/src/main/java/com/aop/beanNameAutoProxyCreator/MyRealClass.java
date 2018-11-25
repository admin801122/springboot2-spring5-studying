package com.aop.beanNameAutoProxyCreator;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshukang
 *
 * 对My开头命名的类进行代理
 * 对execute方法进行代理
 *
 */

@RestController
public class MyRealClass {

    @GetMapping("/execute")
    public void execute(){
        System.out.println("execute...");
        return;
    }
}
