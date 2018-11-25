package com.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;

/**
 * Created by zhangshukang
 */

@SpringBootApplication(exclude = AopAutoConfiguration.class)
public class MyDefaultAdvisor1Application {
    public static void main(String[] args) {
        SpringApplication.run(MyDefaultAdvisor1Application.class);
    }
}
