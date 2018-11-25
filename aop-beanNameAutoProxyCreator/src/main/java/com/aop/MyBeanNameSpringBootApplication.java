package com.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;


//排除掉 springboot 默认开启的aop配置，采用自定义的形式
@SpringBootApplication(exclude = AopAutoConfiguration.class)
public class MyBeanNameSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(MyBeanNameSpringBootApplication.class);
		springApplication.run(args);
	}
}
