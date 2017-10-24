package com.nymph.exception.impl;

import com.nymph.utils.PageCSS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nymph.exception.MethodReturnVoidException;
import com.nymph.exception.RequestInterceptException;
import com.nymph.exception.handle.ExceptionHandler;
import com.nymph.exception.PatternNoMatchException;
import com.nymph.exception.NoSuchClassException;
import com.nymph.context.wrapper.ContextWrapper;

/**
 * 异常处理的实现
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年10月1日下午11:12:31
 */
public class DefaultExceptionHandler implements ExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@Override
	public void handle(ContextWrapper wrapper, Throwable throwable) {
		if (throwable instanceof RequestInterceptException) {
			LOGGER.warn("有一个请求被拦截: {}", wrapper.httpRequest().getRequestURL());
			
		} else if (throwable instanceof PatternNoMatchException) {
			LOGGER.warn(throwable.getMessage(), throwable);
			wrapper.sendError(throwable.getMessage());
			
		} else if (throwable instanceof NoSuchClassException) {
			LOGGER.warn(throwable.getMessage(), throwable);
			wrapper.send404(PageCSS.join(throwable));
			
		} else if (throwable instanceof MethodReturnVoidException) {
			LOGGER.info("当前方法返回值为void 因此直接提交请求");
			
		} else {
			wrapper.sendError(PageCSS.join(throwable));
			LOGGER.error(null, throwable);
			throwable.printStackTrace();
		}
	}

}
