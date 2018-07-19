package com.nymph.annotaion.web;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 表示直接响应HttpBean的方法返回值, 如果返回的是对象会默认转换成JSON字符串
 *     如果是字符串或其他基本类型会直接响应
 */
@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface Body {

}
