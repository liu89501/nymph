package com.nymph.ioc;

/**
 * bean的定义接口, 用于记录bean的信息
 */
public interface BeansDefinition {
    /** 单实例的获取方式 */
    String SINGLETON = "singleton";
    /** 多实例的获取方式 */
    String PROTOTYPE = "prototype";
    /**
     * 获取bean的名字
     * @return
     */
    String getName();

    void setName(String name);

    /**
     * 获取bean的类型
     * @return
     */
    Class<?> getType();

    void setType(Class<?> type);

    /**
     * 获取bean的实例
     * @return
     */
    Object getInstance();

    void setInstance(Object instance);

    /**
     * bean的获取策略
     * @return
     */
    String getPattern();

    void setPattern(String pattern);
}
