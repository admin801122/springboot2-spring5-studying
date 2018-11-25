package com.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;

/**
 * Created by zhangshukang
 */

@SpringBootApplication(exclude = AopAutoConfiguration.class)
public class MyDefaultAdvisor2Application {
    public static void main(String[] args) {
        SpringApplication.run(MyDefaultAdvisor2Application.class);
    }
}
