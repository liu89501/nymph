package com.nymph.ioc.core;

import com.nymph.annotaion.Beans;
import com.nymph.ioc.BeansDefinition;
import com.nymph.utils.AnnoUtils;
import com.nymph.utils.ClassUtils;

import java.lang.annotation.Annotation;

/**
 * BeansDefinition工厂类
 */
public abstract class BeansDefinitionFactory {

    public static BeansDefinition getSimpleDef(String name) throws Exception {
        Object instance = null;
        Class<?> type = ClassUtils.getClass(name);
        String pattern = BeansDefinition.SINGLETON;
        Annotation annotation = AnnoUtils.get(type, Beans.class);
        if (annotation != null) {
            pattern = AnnoUtils.invoke(annotation, "pattern");
        }
        if (!type.isInterface()) {
            instance = ClassUtils.newInstance(type);
        }
        return new SimpleBeansDefinition(name, type, instance, pattern);
    }
}
