package com.nymph.context;

/**
 * 视图解析器将要解析的对象
 * @author NYMPH
 * @date 2017年9月21日下午8:16:39
 */
public class ContextView {

	private ContextWrapper context;
	
	private Object result;
	
	private boolean body;

	public ContextWrapper getContext() {
		return context;
	}

	public Object getResult() {
		return result;
	}

	public boolean isBody() {
		return body;
	}

	public ContextView(ContextWrapper context, Object result, boolean body) {
		this.context = context;
		this.result = result;
		this.body = body;
	}
}
