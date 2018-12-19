package com.beanPostProcessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by zhangshukang.
 */

@SpringBootApplication
public class BeanPostProcessorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BeanPostProcessorApplication.class);

        MyComponent myComponent = applicationContext.getBeanFactory().getBean(MyComponent.class);
        //获取手动添加的字段 value
        System.out.println(myComponent.getCreator());

    }
}
