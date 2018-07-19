package com.nymph.ioc.core;

import com.nymph.ioc.BeansDefinition;
import com.nymph.utils.ClassUtils;

/**
 * bean的定义类, 用于记录bean的信息
 */
public class SimpleBeansDefinition implements BeansDefinition {
    /** bean的名称 */
    private String name;
    /** bean的类型 */
    private Class<?> type;
    /** bean实例 */
    private Object instance;
    /** bean的获取方式,默认单例 */
    private String pattern;

    public SimpleBeansDefinition(String name, Class<?> type, Object instance, String pattern) {
        this.name = name;
        this.type = type;
        this.instance = instance;
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getInstance() {
        if (SINGLETON.equals(pattern)) {
            return instance;
        }
        return ClassUtils.newInstance(type);
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
