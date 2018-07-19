package com.nymph.annotaion.web;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 定义形参的名称, 对应Request中的参数名称, 便于对参数绑定值
 *      由于java1.8之前本身是没有获取形参的api的, 虽然1.8之
 *      后有Parameter api但是有时候也会获取不正确, 所以这里
 *      使用注解来定义参数列表的名字.  如果引入了asm的jar
 *      包的话可以不需要使用此注解, 直接方法参数与请求参数名
 *      称对应即可
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface DefinitionParamName {
    /**
     * ServletRequest中的参数名与形参对应上即可
     * @return
     */
    String[] value();
}
