package com.nymph.annotaion;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 * 标注用, 用于区分Bean的作用	
 */
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
public @interface Components {

}
