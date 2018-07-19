package com.nymph.config;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
/**
 * web相关的配置类
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月2日下午4:19:40
 */
public class WebConfig {
	// 端口号
	private int port;
	// contextPath
	private String contextPath;
	// 过滤器链
	private List<String> filters;
	// 不拦截的url(一般指静态资源)
	private List<String> exclutions;
	// 对应webxml中的<url-pattern>
	private String urlPattern;
	// web跳转url的前缀
	private String suffix;
	// web跳转url的后缀
	private String prefix;
	// 编码过滤器的编码格式, 默认是UTF-8
	private String encoding;
	// webapp的目录
	private String webappPath;
	
	public String getWebappPath() {
		return webappPath;
	}

	public void setWebappPath(String webappPath) {
		this.webappPath = webappPath;
	}

	public String getEncoding() {
		return encoding == null ? "UTF-8" : encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String getSuffix() {
		return suffix == null ? "" : suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getPrefix() {
		return prefix == null ? "" : prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<String> getExclutions() {
		return exclutions == null ? Collections.emptyList() : exclutions;
	}

	public void setExclutions(List<String> exclutions) {
		this.exclutions = exclutions;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContextPath() {
		return contextPath == null ? "" : contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public List<String> getFilters() {
		return filters == null ? Collections.emptyList() : filters;
	}

	public void setFilters(List<String> filters) {
		this.filters = filters;
	}

	public WebConfig(int port, String contextPath, String urlPattern, String encoding) {
		this.port = port;
		this.contextPath = contextPath;
		this.urlPattern = urlPattern;
		this.encoding = encoding;
	}

	public WebConfig() {
	}
}
