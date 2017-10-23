package com.nymph.context;

import com.nymph.context.wrapper.MethodWrapper;

/**
 * 参数解析器	
 * @author NYMPH
 * @date 2017年9月26日2017年9月26日
 */
public interface ParamResolver extends Resolver {
	
	/**
	 * 注入url映射的类的具体方法的所有形参
	 * @param methodWrapper  Method包装类
	 * @return	    		  注入完成的方法参数
	 * @throws Throwable 	  注入时可能出现的异常, 如类型转换异常等
	 */
	Object[] methodParamsInjection(MethodWrapper methodWrapper) throws Throwable;
}