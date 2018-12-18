package com.beanDefinition;

import com.beanDefinition.registrar.EnableMyAutoRegistrar2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by zhangshukang.
 */

@SpringBootApplication
//@EnableMyAutoRegistrar
@EnableMyAutoRegistrar2
public class RegistrarSpringBootApplication{

    public static void main(String[] args) {
        SpringApplication.run(RegistrarSpringBootApplication.class);
    }
}
