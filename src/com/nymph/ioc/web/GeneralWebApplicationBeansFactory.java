package com.nymph.ioc.web;

import com.nymph.ioc.*;
import com.nymph.ioc.component.GeneralBeansComponent;
import com.nymph.ioc.component.EnableBeanProxy;
import com.nymph.ioc.component.EnableBeanRegister;
import com.nymph.ioc.core.BeansPropertyInject;
import com.nymph.ioc.core.BeansClassScanner;
import com.nymph.config.Configuration;
import com.nymph.utils.Prevent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeansFactory的实现	
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月3日下午2:26:28
 */
public class GeneralWebApplicationBeansFactory implements WebApplicationBeansFactory {
	/**
	 *  存放bean实例的容器, 以类名为键
	 */
	private final Map<String, BeansDefinition> container = new ConcurrentHashMap<>(256);
	/**
	 *  HttpBean的容器
	 */
	private MapperInfoContainer httpContainer;
	/**
	 *  bean组件
	 */
	private BeansComponent beansComponent;
	/**
	 *  bean扫描器的实现
	 */
	private BeansClassScanner scanner;
	/**
	 *  bean依赖注入的实现
	 */
	private BeansPropertyInject inject;
	/**
	 *  bean的基础处理器
	 */
	private BeansHandler beansHandler;
	/**
	 *  bean的动态代理处理器
	 */
	private BeansProxy beansProxy;
	/**
	 *  配置文件类
	 */
	private Configuration configuration;

	private void initialized() {
		httpContainer = new MapperInfoContainer(container);
		beansComponent = new GeneralBeansComponent(container);
		scanner = new BeansClassScanner(configuration, container);
		inject = new BeansPropertyInject(container);
	}
	
	/**
	 * 将bean对象放入容器中, 并且为bean对象的@Inject注解字段和方法注入值
	 */
	@Override
	public void register() throws Exception {
		initialized();
		scanner.beansPackageScanner();
		Thread comp = new Thread(beansComponent);
		Thread http = new Thread(httpContainer);
		comp.start();
		http.start();
		comp.join();
		http.join();
		initializedComponent();
		inject.beansDependenceInjection();
		dynamicProxyHandle();
	}

	/**
	 * 初始化组件
	 */
	private void initializedComponent() {
		Prevent<?> proxy = beansComponent.getComponent(EnableBeanProxy.class);
		Prevent<?> register = beansComponent.getComponent(EnableBeanRegister.class);
		setBeansProxy((BeansProxy) proxy.orElse(null));
		if (!register.isNull()) {
			BeansRegister beansRegister = (BeansRegister) register.get();
			container.putAll(beansRegister.registerBeans());
		}
	}

	/**
	 * bean的动态代理处理
	 */
	private void dynamicProxyHandle() {
		if (beansProxy == null) {
			return;
		}
		for (Map.Entry<String, BeansDefinition> entry : container.entrySet()) {
			BeansDefinition definition = entry.getValue();
			Object proxy = beansProxy.proxyBean(definition.getInstance());
			definition.setInstance(proxy);
		}
	}

	@Override
	public Object getBean(String beanName) {
		BeansDefinition definition = container.get(beanName);
		return definition == null ? null : definition.getInstance();
	}
	
	@Override
	public Collection<?> getBeans() {
		return container.values();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> beanClass) {
		Object bean = getBean(beanClass.getName());
		if (bean != null) {
			return (T) bean;
		}

		for (Map.Entry<String, BeansDefinition> entry : container.entrySet()) {
			BeansDefinition definition = entry.getValue();
			if (beanClass.isAssignableFrom(definition.getType())) {
				return (T) definition.getInstance();
			}
		}
		return null;
	}

	@Override
	public MapperInfoContainer getHttpBeansContainer() {
		return httpContainer;
	}

	@Override
	public void setHttpBeansContainer(MapperInfoContainer httpHandler) {
		this.httpContainer = httpHandler;
	}

	@Override
	public BeansHandler getBeansHandler() {
		return beansHandler;
	}

	@Override
	public void setBeansHandler(BeansHandler beansHandler) {
		this.beansHandler = beansHandler;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public BeansComponent getBeansComponent() {
		return beansComponent;
	}

	@Override
	public void setBeansComponent(BeansComponent beansComponent) {
		this.beansComponent = beansComponent;
	}

	@Override
	public BeansProxy getBeansProxy() {
		return beansProxy;
	}

	@Override
	public void setBeansProxy(BeansProxy beansProxy) {
		this.beansProxy = beansProxy;
	}
}
