package com.nymph.start;

import com.nymph.ioc.BeansHandler;
import com.nymph.config.ConfRead;
import com.nymph.context.WebApplicationInitialization;
import com.nymph.utils.ClassUtils;

import javax.servlet.*;
import javax.servlet.ServletRegistration.Dynamic;
import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.List;

/**
 * webxml的启动方式	
 * @author NYMPH
 * @date 2017年10月11日上午11:48:29
 */
public class WebXmlStarter extends WebApplicationInitialization implements ServletContextListener {

	/**
	 *  context param 中的name的名称, web.xml方式时使用的配置,用于指定yml文件的名称
	 */
	private static final String CONFIGURATIONS_PARAM_NAME = "configurations";
	/**
	 * 	bean的环绕增强处理器的定义
	 */
	private static final String BEANS_ROUND_HANDLER_PARAM_NAME = "beanRound";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			// 加载配置文件
			ServletContext context = sce.getServletContext();
			loadConfiguration(context.getInitParameter(CONFIGURATIONS_PARAM_NAME));
			// 实例化bean处理器
			String handler = context.getInitParameter(BEANS_ROUND_HANDLER_PARAM_NAME);
			if (handler != null) {
				BeansHandler beansHandler = ClassUtils.newInstance(handler);
				getBeansFactory().setBeansHandler(beansHandler);
			}
			getBeansFactory().setConfiguration(getConfiguration());
			// 将所有bean对象注册到bean容器
			getBeansFactory().register();
			loadServlets(context);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载必须的servlet
	 * @param context	servletContext对象
	 */
	private void loadServlets(ServletContext context) {
		config = getConfiguration().getWebConfig();
		// 放行静态资源的配置
		List<String> source = config.getExclutions();
		ServletRegistration defaultServlet = context.getServletRegistration("default");
		defaultServlet.addMapping(source.toArray(new String[source.size()]));
		// 加载异步调度器
		Dynamic servlet = context.addServlet("Nymph", CORE_REQUEST_DISPATCHER);
		servlet.addMapping(config.getUrlPattern());
		servlet.setAsyncSupported(true);
		// 加载过滤器
		loadFilters(context, config.getFilters());
		initBaseConfig();
	}
	
	/**
	 * 动态的加载过滤器, 不需要再webxml中配置, 只需要在yml文件中配置全路径即可, 配置多个过滤器时可以形成过滤器链
	 * @param context	ServletContext servlet上下文
	 * @param filters	过滤器的全路径, 可以配置多个
	 */
	private void loadFilters(ServletContext context, List<String> filters) {
		// 加载编码过滤器
		javax.servlet.FilterRegistration.Dynamic encodingFilter =
				context.addFilter("ENCODING", DEFAULT_ENCODING_FILTER);
		encodingFilter.addMappingForUrlPatterns(
				EnumSet.of(DispatcherType.REQUEST), true, "/*");
		encodingFilter.setInitParameter("encoding", config.getEncoding());
		encodingFilter.setAsyncSupported(true);

		for (String filterClass : filters) {
			String urlPattern = "/*";
			if (filterClass.contains("@")) {
				String[] split = filterClass.split("@");
				urlPattern = split[1];
				filterClass = split[0];
			}
			javax.servlet.FilterRegistration.Dynamic dynamic =
					context.addFilter(filterClass, filterClass);

			dynamic.setAsyncSupported(true); // 设置过滤器的异步支持
			dynamic.addMappingForUrlPatterns(
					EnumSet.of(DispatcherType.REQUEST), true, urlPattern);
		}
	}
	
	/**
	 * 加载yml配置文件
	 * @param location yml配置文件的位置
	 */
	private void loadConfiguration(String location) throws FileNotFoundException {
		if (location.indexOf(",") < 0) {
			setConfiguration(ConfRead.readConf(location));
		}
		else {
			setConfiguration(ConfRead.readConf(location.split(",")));
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {}

}
