package com.nymph.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Configuration implements Serializable {

	// 交给容器托管的对象
	private List<Object> component;
	// 关于web的配置
	private WebConfig webConfig;
	// 关于包扫描的配置
	private List<String> scanner;
	// 配置文件的类型(yml | xml)
	private ConfType confType;

	public WebConfig getWebConfig() {
		return webConfig;
	}

	public void setWebConfig(WebConfig webConfig) {
		this.webConfig = webConfig;
	}

	public List<String> getScanner() {
		return scanner == null ? Collections.emptyList() : scanner;
	}

	public void setScanner(List<String> scanner) {
		this.scanner = scanner;
	}

	public List<Object> getComponent() {
		return component == null ? Collections.emptyList() : component;
	}

	public void setComponent(List<Object> component) {
		this.component = component;
	}

	public ConfType getConfType() {
		return confType;
	}

	public void setConfType(ConfType confType) {
		this.confType = confType;
	}

	public void addConfiguration(Configuration configuration) {
		if (component == null) {
			component = new ArrayList<>();
			component.addAll(configuration.getComponent());
		} else {
			component.addAll(configuration.getComponent());
		}
	}
	
}
