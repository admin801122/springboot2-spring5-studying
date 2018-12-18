package com.beanDefinition;

import com.beanDefinition.registrar.EnableMyAutoRegistrar2;
import com.beanDefinition.registrar.EnableMyAutoRegistrar3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.Annotation;

/**
 * Created by zhangshukang.
 */

@SpringBootApplication
//@EnableMyAutoRegistrar
//@EnableMyAutoRegistrar2
@EnableMyAutoRegistrar3
public class RegistrarSpringBootApplication{

    public static void main(String[] args) {

        SpringApplication.run(RegistrarSpringBootApplication.class);
    }
}
