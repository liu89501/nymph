package com.nymph.utils;

import com.nymph.transfer.Multipart;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 关于Class的工具类
 */
public abstract class ClassUtils {

    /**
     * 获取类加载器
     * @return
     */
    public static ClassLoader getClassLoad() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null)
            loader = ClassLoader.getSystemClassLoader();
        if (loader == null)
            loader = BasicUtils.class.getClassLoader();
        return loader;
    }

    /**
     * 获取classpath路径下的资源
     * @param location
     * @return
     */
    public static URL getSource(String location) {
        return getClassLoad().getResource(location.replace(".", "/"));
    }

    /**
     * 根据类路径反射创建对象
     *
     * @param classLocation
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> newInstance(List<String> classLocation) {
        return classLocation.stream().map(ele -> (T)newInstance(ele)).collect(Collectors.toList());
    }

    /**
     * 根据类的全路径创建出一个对象
     * @param className 包名加类名的字符串
     * @return			该类的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            Class<?> forName = Class.forName(className);
            return (T) forName.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据Class创建出一个实例
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<?> clazz) {
        try {
            return (T)clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据类的全限定名来获取Class对象
     * @param className
     * @return
     */
    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字符串转换成和字段对应的类型
     *
     * @param field
     * @param value
     * @return
     */
    public static Object convert(Field field, String value) {
        return convert(field.getType(), value);
    }

    /**
     * 将字符串转成和给定Class对应的类型
     * @param clazz
     * @param value
     * @return
     */
    public static Object convert(Class<?> clazz, String value) {
        if (String.class == clazz) {
            return value;
        } else if (int.class == clazz || Integer.class == clazz) {
            return Integer.valueOf(value);
        } else if (boolean.class == clazz || Boolean.class == clazz) {
            return Boolean.valueOf(value);
        } else if (double.class == clazz || Double.class == clazz) {
            return Double.valueOf(value);
        } else if (long.class == clazz || Long.class == clazz) {
            return Long.valueOf(value);
        } else if (byte.class == clazz || Byte.class == clazz) {
            return Byte.valueOf(value);
        } else if (float.class == clazz || Float.class == clazz) {
            return Float.valueOf(value);
        } else if (short.class == clazz || Short.class == clazz) {
            return Short.valueOf(value);
        } else if (char.class == clazz || Character.class == clazz) {
            return value.charAt(0);
        }
        return null;
    }

    /**
     * 将一个String数组转换为clazz类型的list集合
     * @param clazz
     * @param array
     * @return
     */
    public static List<?> convertList(Class<?> clazz, String[] array) {
        if (String[].class.equals(clazz)) {
            return Arrays.stream(array).collect(Collectors.toList());
        } else if (Integer[].class.equals(clazz)) {
            return Arrays.stream(array).map(ele -> Integer.valueOf(ele)).collect(Collectors.toList());
        } else if (Long[].class.equals(clazz)) {
            return Arrays.stream(array).map(ele -> Long.valueOf(ele)).collect(Collectors.toList());
        } else if (Boolean[].class.equals(clazz)) {
            return Arrays.stream(array).map(ele -> Boolean.valueOf(ele)).collect(Collectors.toList());
        } else if (Double[].class.equals(clazz)) {
            return Arrays.stream(array).map(ele -> Double.valueOf(ele)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * @备注: 判断集合的泛型是否是普通 类型
     * @param type
     * @return
     */
    public static boolean isCommonCollection(Type type) {
        if (type == null)
            return false;
        Class<?> clazz = (Class<?>) ((ParameterizedType)type).getActualTypeArguments()[0];
        if (Number.class.isAssignableFrom(clazz) || String.class.isAssignableFrom(clazz)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个Class是否是集合类型
     *
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {
        if (Collection.class.isAssignableFrom(clazz))
            return true;
        return false;
    }

    /**
     * 判断一个Class是否是基本类型(包含了String)
     * @param clazz
     * @return
     */
    public static boolean isCommonType(Class<?> clazz) {
        return String.class == clazz ||
                int.class == clazz || Integer.class == clazz ||
                boolean.class == clazz || Boolean.class == clazz ||
                double.class == clazz || Double.class == clazz ||
                long.class == clazz || Long.class == clazz ||
                byte.class == clazz || Byte.class == clazz ||
                float.class == clazz || Float.class == clazz ||
                short.class == clazz || Short.class == clazz ||
                char.class == clazz || Character.class == clazz;
    }

    /**
     * 判断一个字段是否是基本类型(包含String)
     * @param field
     * @return
     */
    public static boolean isCommonType(Field field) {
        return isCommonType(field.getType());
    }

    /**
     * 判断当前应用是否是jar包的形式启动
     * @return
     */
    public static boolean isJarLaunch() {
        URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        URL[] urls = classLoader.getURLs();
        if (urls.length == 1 &&
                ("jar".equals(urls[0].getProtocol()) ||
                        urls[0].toString().endsWith(".jar"))) {

            return true;
        } else {
            return false;
        }
    }
}
