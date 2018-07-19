/**
 * 
 */
package com.nymph.annotaion;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * 作用与Bean相同, 用于区分Web项目三层结构, 一般用在Http映射类上
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月22日下午3:09:22
 */
@Retention(RUNTIME)
@Target(TYPE)
@Beans
public @interface Http {

	/**
	 * 等同于namespace
	 * @return
	 */
	String value() default "";

	/**
	 * 获取策略, 分单例和多例
	 * @return
	 */
	String pattern() default "singleton";
}
