package com.beanPostProcessor.component;

import org.springframework.stereotype.Component;

/**
 * Created by zhangshukang on 2018/12/19.
 */

@Component
public class MyComponent {


    static {
        System.out.println("myComponent init...");
    }


    private String creator;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
