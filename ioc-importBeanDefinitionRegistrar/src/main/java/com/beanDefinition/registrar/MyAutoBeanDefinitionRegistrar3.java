package com.beanDefinition.registrar;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by zhangshukang.
 *
 * 高级用法，模拟 FeignClientsRegistrar 注册方式
 */
public class MyAutoBeanDefinitionRegistrar3 implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware {


    private ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scan = getScanner();

        //zhiding指定注解，类似于Feign注解
        scan.addIncludeFilter(new AnnotationTypeFilter(MyComponent.class));
        Set<BeanDefinition> candidateComponents = scan.findCandidateComponents("com.beanDefinition.registrar.component");

        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

        candidateComponents.stream().forEach(beanDefinition -> {

            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            if (!registry.containsBeanDefinition(beanDefinition.getBeanClassName())) {
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        });
    }


    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {

            // FeignClient 重写了 ClassPathScanningCandidateComponentProvider 匹配逻辑
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    // TODO until SPR-11711 will be resolved
                    // 判断接口是否继承了 Annotation注解
                    if (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata()
                            .getInterfaceNames().length == 1 && Annotation.class.getName().equals(beanDefinition.getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(beanDefinition.getMetadata().getClassName(),
                                    MyAutoBeanDefinitionRegistrar3.this.classLoader);
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            this.logger.error(
                                    "Could not load target class: " + beanDefinition.getMetadata().getClassName(), ex);
                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }


}
