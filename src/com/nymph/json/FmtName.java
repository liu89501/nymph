package com.nymph.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 指定被解析字段的名字, 返回的JSON字符串的字段名以该注解的值为准
 *
 * @FmtName("agea")
 * private int age;
 *
 *
 * 上面的例子解析的json字符串的字段名会变成agea
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface FmtName {
	
	String value() default "";

}
