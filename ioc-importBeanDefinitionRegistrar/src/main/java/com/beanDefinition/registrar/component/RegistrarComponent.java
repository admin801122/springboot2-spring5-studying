package com.beanDefinition.registrar.component;

import com.beanDefinition.registrar.MyComponent;

/**
 * Created by zhangshukang.
 */

@MyComponent
public class RegistrarComponent {

    static {
        System.out.println("registrarComponent init...");
    }

}
