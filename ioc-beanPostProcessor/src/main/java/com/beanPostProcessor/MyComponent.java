package com.beanPostProcessor;

import org.springframework.stereotype.Component;

/**
 * Created by zhangshukang on 2018/12/19.
 */

@Component
public class MyComponent {

    private String creator;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
