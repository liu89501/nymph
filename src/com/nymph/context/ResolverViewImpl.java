package com.nymph.context;

import com.nymph.context.core.ResolverView;
import com.nymph.utils.BasicUtils;
import com.nymph.utils.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nymph.json.JSONUtil;
/**
 * 视图解析器的实现
 * @date: 2017年9月17日
 * @author: Nymph
 */
public class ResolverViewImpl extends AbstractResolver implements ResolverView {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResolverViewImpl.class);
	/** 
	 * HttpBean的返回值后缀 
	 */
	private static final String PATH_SUFFIX = configuration.getWebConfig().getSuffix();
	/** 
	 * HttpBean的返回值前缀
	 */
	private static final String PATH_PREFIX = configuration.getWebConfig().getPrefix();
	/** 
	 * 视图参数 
	 */
	private final ContextView contextView;
	/**
	 *  表示web层方法返回结果的类型 分下面四种
	 */
	private int typeCode;
	/**
	 *  表示转发请求
	 */
	private static final int REQUEST_FORWRAD = 1;
	/**
	 *  请求重定向(相对路径)
	 */
	private static final int RELATIVE_REDIRECT = 2;
	/**
	 *  请求重定向(绝对路径)
	 */
	private static final int ABSOLUTE_REDIRECT = 3;
	/**
	 *  当返回的是一个基本类型和String之外的其他对象时, 那么会将
	 * 			这个对象解析为json字符串
	 */
	private static final int RESPONSE_JSON = 4;
	/**
	 *  当目标方法的返回值是个String时, 即使这个字符串是json格式
	 * 			这里也并不会将它当Json处理, 而是直接原封不动的响应给页面
	 */
	private static final int RESPONSE_HTML = 5;

	/**
	 * 相对路径重定向的前缀符号
	 */
	private static final String RELATIVE_REDIRECT_PREFIX = "->";

	/**
	 * 绝对路径重定向的前缀符号
	 */
	private static final String ABSOLUTE_REDIRECT_PREFIX = "-->";

	
	public ResolverViewImpl(ContextView contextView) {
		super(contextView.getContext());
		this.contextView = contextView;
	}
	
	@Override
	public void resolver() throws Throwable {
		String result = dispatchTypeHandle(contextView.getResult());
		
		switch (typeCode) {
		case REQUEST_FORWRAD:
			result = result.startsWith("/") ? result : "/" + result;
			wrapper.forward(result);
			break;
		case RELATIVE_REDIRECT:
			wrapper.relativeRedirect(result);
			wrapper.complete();
			break;
		case ABSOLUTE_REDIRECT:
			wrapper.absoluteRedirect(result);
			wrapper.complete();
			break;
		case RESPONSE_JSON:
			wrapper.sendJSON(result);
			wrapper.complete();
			break;
		case RESPONSE_HTML:
			wrapper.sendHtml(result);
			wrapper.complete();
			break;
		default:
			// default response html
			wrapper.sendHtml(result);
			wrapper.complete();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("view result: - {}", result);
		}
	}
	
	@Override
	public String dispatchTypeHandle(Object result) {
		if (contextView.isBody()) {
			/**
			 * 目标方法的返回值是除基本类型和String类型
			 * 的其他类型时会将这个对象解析成Json字符串
			 */
			if (result != null && !ClassUtils.isCommonType(result.getClass())) {
				typeCode = RESPONSE_JSON;
				return JSONUtil.resolve(result);
			}
			/**
			 * 当目标方法的返回值是String或者其他
			 * 基本类型时不会解析成Json, 而是直接响应
			 */
			typeCode = RESPONSE_HTML;
			return String.valueOf(result);
		}
		checkReturnValue(result);
		// 转发请求或重定向时的处理
		// '->'表示以相对路径重定向 '-->'表示以整个url来重定向
		String url = String.valueOf(result);
		if (url.startsWith(RELATIVE_REDIRECT_PREFIX)) {
			typeCode = RELATIVE_REDIRECT;
			url = url.replace(RELATIVE_REDIRECT_PREFIX, "");
		} else if (url.startsWith(ABSOLUTE_REDIRECT_PREFIX)) {
			typeCode = ABSOLUTE_REDIRECT;
			url = url.replace(ABSOLUTE_REDIRECT_PREFIX, "");
		} else {
			typeCode = REQUEST_FORWRAD;
			url = PATH_PREFIX + url + PATH_SUFFIX;
		}
		return url;
	}

	/**
	 * 返回值检查
	 * @param value
	 */
	private void checkReturnValue(Object value) {
		if (value == null) {
			throw new IllegalArgumentException("method return value couldn't is [null]");
		}
	}

}
