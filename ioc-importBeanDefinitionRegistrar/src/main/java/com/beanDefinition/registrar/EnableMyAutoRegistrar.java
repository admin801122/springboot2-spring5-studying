package com.beanDefinition.registrar;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Created by zhangshukang on 2018/12/18.
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MyAutoBeanDefinitionRegistrar.class)
public @interface EnableMyAutoRegistrar {
}
