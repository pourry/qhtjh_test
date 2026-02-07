package com.example.spring_boot_mode.test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class AnnotationDrivenValidationAspect {

    // 针对有@ValidateParams注解的方法
    @Pointcut("@annotation(validateParams)")
    public void annotatedMethods(ValidateParams validateParams) {}

    @Around("annotatedMethods(validateParams)")
    public Object validateWithAnnotation(ProceedingJoinPoint joinPoint,
                                         ValidateParams validateParams) throws Throwable {

        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        // 执行注解定义的校验规则
        if (!validateByAnnotation(args, method, validateParams)) {
            System.out.println("注解校验失败，方法被跳过: " + method.getName());
return false;

//            // 如果不跳过，可以抛异常
//            throw new IllegalArgumentException("参数校验失败");
        }

        return joinPoint.proceed();
    }

    private boolean validateByAnnotation(Object[] args, Method method,
                                         ValidateParams annotation) {
        // 获取参数名（需要编译时开启-parameters或使用Spring的ParameterNameDiscoverer）
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            // 非空校验
            if (annotation.notNull() && arg == null) {
                System.out.println("参数不能为null: " + parameters[i].getName());
                return false;
            }

            // 数值范围校验
            if (arg instanceof Number) {
                double value = ((Number) arg).doubleValue();
                if (value < annotation.minValue() || value > annotation.maxValue()) {
                    System.out.println("参数值超出范围: " + value);
                    return false;
                }
            }

            // 字符串正则校验
            if (arg instanceof String && !annotation.regex().isEmpty()) {
                String str = (String) arg;
                if (!str.matches(annotation.regex())) {
                    System.out.println("字符串格式不匹配: " + str);
                    return false;
                }
            }
        }

        return true;
    }

}