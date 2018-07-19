package com.nymph.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * JavaBean的工具类
 * @author NYMPH
 * @date 2017年10月7日下午8:33:21
 */
public abstract class BeanUtils {
    /**
     * copy符合JavaBean规范的对象
     *
     * @param source 源对象
     * @param target 要copy的对象
     * @return copy完成的对象
     */
    public static <T> T copy(T source, T target) {
        try {
            return copy0(source, target, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    /**
     * copy符合JavaBean规范的对象
     * @param source            源对象
     * @param target            要copy的对象
     * @param excludeField      忽略copy的字段名
     * @return
     */
    public static <T> T copy(T source, T target, String excludeField) {
        try {
            return copy0(source, target, excludeField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    /**
     * copy符合JavaBean规范的对象
     *
     * @param source  源对象
     * @param target  要copy的对象
     * @param exclude 要忽略的方法名
     * @return copy完成的对象
     */
    static <T> T copy0(Object source, T target, String exclude) throws InvocationTargetException, IllegalAccessException {
        Method[] sourceMethod = source.getClass().getMethods();
        Method[] targetMethod = target.getClass().getMethods();

        for (Method sourceVal : sourceMethod) {
            final String sourceName = sourceVal.getName();
            if (! sourceName.startsWith("get") ||
                    sourceName.toUpperCase().endsWith(exclude.toUpperCase()))
                continue;
            for (Method targetVal : targetMethod) {
                String targetName = targetVal.getName();
                if (! targetName.startsWith("set"))
                    continue;

                targetName = targetName.replace("set", "");
                String replace = sourceName.replace("get", "");

                if (targetName.equals(replace)) {
                    Object invoke = sourceVal.invoke(source);
                    if (invoke == null)
                        continue;
                    targetVal.invoke(target, invoke);
                }
            }
        }
        return target;
    }
}
