package com.nymph.utils;

/**
 * 防止空指针的类
 */
public final class Prevent<T> {

    private T primitive;

    public static <T> Prevent<T> of(T value) {
        return empty(value);
    }

    public static <T> Prevent<T> ofEmpty() {
        return empty(null);
    }
    /**
     * 获取primitive的值, 如果为null时返回默认值
     * @param defaultVal
     * @return
     */
    public T orElse(T defaultVal) {
        if (primitive == null) {
            return defaultVal;
        }
        return primitive;
    }

    /**
     * 获取primitive
     * @return
     */
    public T get() {
        if (primitive == null)
            throw new IllegalArgumentException("primitive require not null");
        return primitive;
    }

    /**
     * 判断primitive是否为null
     * @return
     */
    public boolean isNull() {
        return primitive == null;
    }

    /**
     * 获取一个primitive为null的实例
     * @return
     */
    static <T> Prevent<T> empty(T value) {
        Prevent<T> prevent = new Prevent<>();
        prevent.primitive = value;
        return prevent;
    }

}
