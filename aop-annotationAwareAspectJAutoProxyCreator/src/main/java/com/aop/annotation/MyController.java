package com.aop.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangshukang
 */
@RestController
public class MyController {


    @Autowired MyAnnotationService myAnnotationService;

    @GetMapping("/ok")
    public Object ok(){
        myAnnotationService.myAnnotationServiceExecute();
        return "ok";
    }
}
