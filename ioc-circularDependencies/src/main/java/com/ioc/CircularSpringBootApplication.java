package com.ioc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;

/**
 * Created by zhangshukang.
 */

@SpringBootApplication
public class CircularSpringBootApplication implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {


//
//
//    circularSpringBootApplication (field com.ioc.circular.Service1 com.ioc.CircularSpringBootApplication.service1)
//┌─────┐
//        |  service1 (field com.ioc.circular.Service2 com.ioc.circular.Service1.service2)
//↑     ↓
//        |  service2 (field com.ioc.circular.Service3 com.ioc.circular.Service2.service3)
//↑     ↓
//        |  service3 (field com.ioc.circular.Service1 com.ioc.circular.Service3.service1)
//└─────┘


    public static void main(String[] args) {
        SpringApplication.run(CircularSpringBootApplication.class);
    }

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setPort(9999);
    }
}
