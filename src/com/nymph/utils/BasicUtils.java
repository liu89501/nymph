package com.nymph.utils;

import java.util.Collections;
import java.util.List;

/**
 * 常用操作的工具类	
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月2日下午8:34:10
 */
public abstract class BasicUtils {
	
	/**
	 * 关闭各种连接资源
	 * 
	 * @param closeable
	 */
	public static void closed(AutoCloseable... closeable) {
		for (AutoCloseable autoCloseable : closeable) {
			try {
				if (autoCloseable != null) {
					autoCloseable.close();
				}
			} catch (Exception e) {}
		}
	}

	/**
	 * 判断一个数组是否为空和长度是否为0
	 * @param target
	 * @return
	 */
	public static boolean notNullAndLenNotZero(Object[] target) {
		if (target == null || target.length == 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * 如果目标对象为null则返回默认对象
	 * @param target	
	 * @param defaults
	 * @return
	 */
	public static <T> T ifNullDefault(T target, T defaults) {
		if (target == null) {
			return defaults;
		}
		return target;
	}
	
	/**
	 * 如果List为null则返回一个空的List
	 * @param list
	 * @return
	 */
	public static <T>List<T> ofNullList(List<T> list) {
		return list == null ? Collections.emptyList() : list;
	}
	
}
