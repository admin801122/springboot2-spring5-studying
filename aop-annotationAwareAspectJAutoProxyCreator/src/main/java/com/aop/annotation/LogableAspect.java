package com.aop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogableAspect {

    @Pointcut("@annotation(com.aop.annotation.Logable)")
    public void aspect() {
    }

    @Around("aspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {

        Object target = point.getTarget();

        System.out.println("doAround before...");

        Object returnValue =  point.proceed(point.getArgs());

        System.out.println("doAround after...");
        return returnValue;
    }
}
