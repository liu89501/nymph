package com.nymph.ioc;

import java.util.Map;

/**
 * bean的注册接口
 */
public interface BeansRegister {

    /**
     * 将bean的集合注册到bean工厂
     * @return  存放bean的map集合, key应该为bean的完整名称(包名+类名), value为bean实例
     */
    Map<String, BeansDefinition> registerBeans();
}
