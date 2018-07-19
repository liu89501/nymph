package com.nymph.ioc.component;

import com.nymph.annotaion.Beans;
import com.nymph.annotaion.Components;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Beans
@Components
public @interface EnableBeanRegister {
}
