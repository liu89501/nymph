package com.nymph.ioc.component;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.nymph.annotaion.Beans;
import com.nymph.annotaion.Components;

@Retention(RUNTIME)
@Target(TYPE)
@Beans
@Components
public @interface EnableBeanProxy {

}
