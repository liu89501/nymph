/**
 * 
 */
package com.nymph.annotaion.web;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * 用于标记4种请求方式
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月22日下午3:21:49
 */
@Retention(RUNTIME)
@Target({ ANNOTATION_TYPE })
public @interface Method {
}
