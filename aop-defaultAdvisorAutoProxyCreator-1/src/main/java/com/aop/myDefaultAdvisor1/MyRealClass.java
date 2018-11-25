package com.aop.myDefaultAdvisor1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshukang
 *
 */

@RestController
public class MyRealClass {

    public MyRealClass(){
        System.out.println("myRealClass init...");
    }

    @GetMapping("/execute")
    public void execute(){
        System.out.println("execute...");
        return;
    }
}
