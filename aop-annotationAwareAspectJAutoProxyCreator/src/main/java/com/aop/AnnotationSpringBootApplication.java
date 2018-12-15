package com.aop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = {TransactionAutoConfiguration.class})
@RestController
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AnnotationSpringBootApplication implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {


	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(AnnotationSpringBootApplication.class);
		springApplication.run(args);
	}

	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		factory.setPort(9999);
	}

}
