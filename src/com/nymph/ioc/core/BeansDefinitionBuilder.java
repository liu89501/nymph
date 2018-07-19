package com.nymph.ioc.core;

import com.nymph.ioc.BeansDefinition;
import com.nymph.utils.ClassUtils;

/**
 * BeansDefinition的构建类
 */
public class BeansDefinitionBuilder {
    /** bean的名称 */
    private String name;
    /** bean的类型 */
    private Class<?> type;
    /** bean实例 */
    private Object instance;
    /** bean的获取方式,默认单例 */
    private String pattern;

    public BeansDefinitionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BeansDefinitionBuilder setType(Class<?> type) {
        this.type = type;
        return this;
    }

    public BeansDefinitionBuilder setInstance(Object instance) {
        this.instance = instance;
        return this;
    }

    public BeansDefinitionBuilder setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public BeansDefinition builder() {
        instance = ClassUtils.newInstance(type);
        return new SimpleBeansDefinition(name, type, instance, pattern);
    }
}
