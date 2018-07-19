package com.nymph.ioc;

import com.nymph.utils.Prevent;

import java.lang.annotation.Annotation;
import java.util.List;

public interface BeansComponent extends Runnable {
	/**
	 * 根据注解类型获取所有component
	 * @param anno	指定的注解
	 * @return		对应的bean组件集合
	 */
	List<?> getComponents(Class<? extends Annotation> anno);

	/**
	 * 根据注解类型获取单个component
	 * @param anno
	 * @return
	 */
	Prevent<?> getComponent(Class<? extends Annotation> anno);

	/**
	 * 过滤出组件的Bean
	 */
	void filterComponents();

}
