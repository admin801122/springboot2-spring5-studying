package com.aop.beanNameAutoProxyCreator;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangshukang
 */

@Configuration
@AutoConfigureOrder
@AutoConfigureBefore
public class BeanNameAutoProxyCreatorConfig {


    /**
     * 注意，这里不能用 @ConditionalOnBean(NameMatchMethodPointcutAdvisor.class) 注解，当被依赖的bean 如果也是配置类中的配置的话，
     * 会出现 bean 先后初始化顺序的问题。另外大部分的bean几乎都是单例方式存在的，所以完全可以用 @ConditionalOnClass 代替。
     * 因为beanNameAutoProxyCreator 的初始化在 NameMatchMethodPointcutAdvisor 之前，所以在加载 @ConditionalOnBean的时候，
     * 被依赖的bean还未初始化，所以条件注解会返回 false。（使用条件注解的同学需要注意，这里算是一个学习记录。）。
     *
     */

    @Bean
    @ConditionalOnClass(NameMatchMethodPointcutAdvisor.class)
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator(){

        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        //beanNames尽可能的精确，以免匹配到底层框架的类。否则会报错，因为有些底层框架的类不能被代理。
        //当前目录下会命中 MyRealClass 类
        beanNameAutoProxyCreator.setBeanNames("my*");
        //设置 advisor，可以理解为设置通知
        beanNameAutoProxyCreator.setInterceptorNames("nameMatchMethodPointcutAdvisor");
        return beanNameAutoProxyCreator;

    }

}
