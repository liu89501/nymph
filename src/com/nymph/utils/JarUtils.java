package com.nymph.utils;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * Jar包相关工具类
 * @author Nymph
 * @date 2017年10月7日下午8:32:40
 */
public abstract class JarUtils {
	
	/**
	 * 根据给出的包路径寻找jar包
	 * @param jarLocation
	 * @return
	 */
	public static JarFile getJarFile(String jarLocation) throws IOException {
		String packages = jarLocation.replace('.', '/');
		URL url = ClassUtils.getClassLoad().getResource(packages);
		if (url != null && "jar".equals(url.getProtocol())) {
			JarURLConnection connection = (JarURLConnection) url.openConnection();
			return connection.getJarFile();
		}
		return null;
	}
}
