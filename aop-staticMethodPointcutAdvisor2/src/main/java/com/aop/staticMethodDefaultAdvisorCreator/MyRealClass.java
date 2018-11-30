package com.aop.staticMethod;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshukang
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
