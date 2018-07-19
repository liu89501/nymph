package com.nymph.ioc.component;

import java.lang.annotation.Annotation;
import java.util.*;

import com.nymph.annotaion.Components;
import com.nymph.ioc.BeansComponent;
import com.nymph.ioc.BeansDefinition;
import com.nymph.utils.AnnoUtils;
import com.nymph.utils.Prevent;

/**
 * bean组件的处理的实现
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月23日上午11:49:38
 */
public class GeneralBeansComponent implements BeansComponent {
	/**
	 *  存放组件的容器
	 */
	private final Map<Class<?>, List<Object>> components = new HashMap<>();

	private final Map<String, BeansDefinition> container;

	public GeneralBeansComponent(Map<String, BeansDefinition> container) {
		this.container = container;
	}

	@Override
	public List<?> getComponents(Class<? extends Annotation> anno) {
		List<?> list = components.get(anno);
		return list == null ? Collections.emptyList() : list;
	}

	@Override
	public Prevent<?> getComponent(Class<? extends Annotation> anno) {
		List<?> list = components.get(anno);
		return list == null || list.size() == 0 ? Prevent.ofEmpty() : Prevent.of(list.get(0));
	}

	@Override
	public void filterComponents() {
		for (Map.Entry<String, BeansDefinition> entry : container.entrySet()) {
			BeansDefinition definition = entry.getValue();
			Class<?> anno = AnnoUtils.getType(definition.getType(), Components.class);
			if (anno == null) {
				return;
			}

			List<Object> comps;
			if ((comps = components.get(anno)) != null) {
				comps.add(definition.getInstance());
			} else {
				comps = new LinkedList<>();
				comps.add(definition.getInstance());
				components.put(anno, comps);
			}
		}
	}

	@Override
	public void run() {
		filterComponents();
	}
}
