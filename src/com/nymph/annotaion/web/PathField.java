package com.nymph.annotaion.web;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * 声明一个与url路径对应的变量占位符
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月3日下午1:39:32
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface PathField {

	/**
	 * 比如/demo/@test, 在方法的形参内可以用@PathField("test") String xxx
	 *     来获取到@test的实际值, 如果形参名和声明的变量名字一样可以不需要指定,如@PathField String test
	 */
	String value() default "";
}
