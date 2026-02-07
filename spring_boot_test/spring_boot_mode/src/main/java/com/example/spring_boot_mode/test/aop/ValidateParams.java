package com.example.spring_boot_mode.test.aop;

import java.lang.annotation.*;

// 参数校验注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateParams {
    // 校验规则：非空
    boolean notNull() default false;

    // 最小值（针对数字）
    double minValue() default Double.MIN_VALUE;

    // 最大值（针对数字）
    double maxValue() default Double.MAX_VALUE;

    // 正则表达式（针对字符串）
    String regex() default "";

    // 跳过方法执行（当校验失败时）
    boolean skipOnInvalid() default true;
}