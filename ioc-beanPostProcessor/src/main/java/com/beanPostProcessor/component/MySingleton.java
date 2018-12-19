package com.beanPostProcessor.component;

/**
 * Created by zhangshukang on 2018/12/19.
 */

//手动注册单例 bean
public class MySingleton {

    static {
        System.out.println("mySingleton init...");

    }
}
