package com.nymph.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * 加载配置文件(xml或者yml)
 * @author LiuYang, LiangTianDong
 * @date 2017年10月29日下午8:57:19
 */
public class ConfRead {
	
	public static Configuration readConf(String... location) throws FileNotFoundException {
		Configuration configuration = null;
		for (String file : location) {
			if (file.endsWith(".xml")) {
				Configuration conf = XmlRead.readXml(file);
				if (configuration == null)
					configuration = conf;
				else
					configuration.addConfiguration(conf);
			} else {
				Configuration conf = YmlRead.readYml(file);
				if (configuration == null)
					configuration = conf;
				else
					configuration.addConfiguration(conf);
			}
		}
		return configuration;
	}

	public static Configuration readConf(InputStream... streams) {
		Configuration configuration = null;
		for (InputStream stream : streams) {
			try {
				if (configuration == null)
					configuration = XmlRead.readXml(stream);
				else
					configuration.addConfiguration(XmlRead.readXml(stream));
			} catch (Exception e) {
				e.printStackTrace();
				if (configuration == null)
					configuration = YmlRead.readYml(stream);
				else
					configuration.addConfiguration(YmlRead.readYml(stream));
			}
		}
		return configuration;
	}

	public static Configuration readConfStream(List<InputStream> streams) {
		return readConf(streams.toArray(new InputStream[streams.size()]));
	}

	public static Configuration readConfLocation(List<String> location) throws FileNotFoundException {
		return readConf(location.toArray(new String[location.size()]));
	}
}
