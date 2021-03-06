package com.nymph.ioc;

/**
 * Bean的代理处理接口
 * @author LiuYang
 * @author LiangTianDong
 */
public interface BeansProxy {
	/**
	 * 可以在此返回代理对象注册到bean工厂
	 * @param bean	bean的实例
	 * @return		代理后的对象
	 */
	Object proxyBean(Object bean);

}
