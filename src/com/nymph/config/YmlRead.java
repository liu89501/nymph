package com.nymph.config;

import com.nymph.utils.ClassUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 加载配置文件
 *
 * @author LiuYang, LiangTianDong
 * @date 2017年9月24日下午4:08:05
 */
public final class YmlRead {

    private static final Class<Configuration> CONFIG_CLASS = Configuration.class;

    private static final Yaml yaml = new Yaml();

    /**
     * 加载yml配置文件
     *
     * @param locations
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Configuration readYml(String... locations) throws FileNotFoundException {
        Configuration configuration = null;
        for (String path : locations) {
            if (configuration == null) {
                configuration = readYml(new FileInputStream(path));
            } else {
                configuration.addConfiguration(readYml(new FileInputStream(path)));
            }
        }
        return configuration;
    }

    /**
     * 加载yml配置文件
     *
     * @param streams
     * @return
     */
    public static Configuration readYml(InputStream... streams) {
        Configuration configuration = null;
        for (InputStream stream : streams) {
            if (configuration == null) {
                configuration = yaml.loadAs(stream, CONFIG_CLASS);
                configuration.setConfType(ConfType.YML);
            } else {
                configuration.addConfiguration(yaml.loadAs(stream, CONFIG_CLASS));
            }
        }
        return configuration;
    }

}
