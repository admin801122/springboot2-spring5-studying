package com.aop.regexpMethodPointcutAdvisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;

/**
 * Created by zhangshukang
 */

@SpringBootApplication(exclude = AopAutoConfiguration.class)
public class MyRegexpAdvisorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyRegexpAdvisorApplication.class);
    }
}
