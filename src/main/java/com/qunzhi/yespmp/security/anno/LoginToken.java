package com.qunzhi.yespmp.security.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title:  token放行
 * @author: Yuanbo
 * @date: 2019/8/1  17:26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginToken {
    boolean required() default true;
}
