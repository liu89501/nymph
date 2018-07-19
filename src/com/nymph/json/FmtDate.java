package com.nymph.json;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * json解析时的时间格式
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface FmtDate {

    /**
     * 时间格式 (类似yyyy-MM-dd  /  yyyy-MM-dd HH:mm:ss)
     * @return
     */
    String value() default "";
}
