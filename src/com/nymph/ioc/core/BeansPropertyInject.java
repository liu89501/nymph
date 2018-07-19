package com.nymph.ioc.core;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import com.nymph.annotaion.Inject;
import com.nymph.ioc.BeansDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注入bean中被@Inject注解标识的字段或方法
 *
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月3日下午2:37:44
 */
public class BeansPropertyInject {

    private static final Logger LOG = LoggerFactory.getLogger(BeansPropertyInject.class);

    private Map<String, BeansDefinition> beanContainer;

    public void beansDependenceInjection() throws Exception {
        fieldInjection();
    }

    public BeansPropertyInject(Map<String, BeansDefinition> beanContainer) {
        this.beanContainer = beanContainer;
    }

    /**
     * bean的字段注入(当发现@Inject注解时)
     *
     * @throws Exception
     */
    public void fieldInjection() throws Exception {
        for (Entry<String, BeansDefinition> entry : beanContainer.entrySet()) {
            BeansDefinition definition = entry.getValue();
            Object bean = definition.getInstance();
            String beanName = entry.getKey();
            Field[] fields = definition.getType().getDeclaredFields();

            for (Field field : fields) {
                if (!field.isAnnotationPresent(Inject.class))
                    continue;

                Object inject;
                if ((inject = findBean(field.getType())) == null)
                    continue;

                field.setAccessible(true);
                field.set(bean, inject);
                LOG.info("inject [{}] -> [{} {}]",
                        field.getType().getName(), beanName, field.getName());
            }
        }
    }

    /**
     * 判断bean容器中是否包含指定类型的对象
     *
     * @param target
     * @return
     */
    private Object findBean(Class<?> target) {
        BeansDefinition definition = beanContainer.get(target.getName());
        if (definition != null) {
            return definition.getInstance();
        }

        for (Entry<String, BeansDefinition> kv : beanContainer.entrySet()) {
            if (target.isAssignableFrom(kv.getValue().getType())) {
                return kv.getValue().getInstance();
            }
        }
        return null;
    }
}
