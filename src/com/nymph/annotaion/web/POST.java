package com.nymph.annotaion.web;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Http协议的Post请求方式
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月22日下午3:18:25
 */
@Retention(RUNTIME)
@Target(METHOD)
@Method
public @interface POST {

	/**
	 * url的路径
	 * @return
	 */
	String value() default "";

}
