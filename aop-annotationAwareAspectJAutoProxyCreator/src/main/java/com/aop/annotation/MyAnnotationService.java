package com.aop.annotation;
        import org.springframework.stereotype.Service;

/**
 * Created by zhangshukang
 */

@Service
public class MyAnnotationService implements Testtttt{

    public MyAnnotationService(){
        System.out.println("init service");
    }

    public void myaaaaaaaat(){
        System.out.println("myAnnotationServiceExecute");
    }

    @Logable
    public void myAnnotationServiceExecute(){
        System.out.println("myAnnotationServiceExecute");
    }
}
