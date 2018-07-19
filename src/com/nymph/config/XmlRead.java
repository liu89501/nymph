package com.nymph.config;

import com.nymph.utils.ClassUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xml配置文件的读取
 *
 * @author LiuYang, LiangTianDong
 * @date 2017年10月29日下午3:59:42
 */
public final class XmlRead {
    // dom4j api
    private static final SAXReader SAX = new SAXReader();

    /**
     * 读取多个xml配置文件
     *
     * @param locations
     * @return
     */
    public static Configuration readXml(String... locations) {
        try {
            Configuration configuration = null;
            for (String location : locations) {
                if (configuration == null) {
                    configuration = readXml(new FileInputStream(location));
                } else {
                    configuration.addConfiguration(readXml(new FileInputStream(location)));
                }
            }
            return configuration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取多个xml配置文件
     *
     * @param streams
     * @return
     */
    public static Configuration readXml(InputStream... streams) {
        try {
            Configuration configuration = null;
            for (InputStream stream : streams) {
                Document document = SAX.read(stream);
                Element element = document.getRootElement();
                if (configuration == null) {
                    configuration = read(element);
                    configuration.setConfType(ConfType.XML);
                } else {
                    configuration.addConfiguration(read(element));
                }
            }
            return configuration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Configuration read(Element element) throws Exception {
        Configuration configuration = new Configuration();

        // 组件
        Element sPackage = element.element("package");
        if (sPackage != null) {
            List<Element> list = sPackage.elements("scanner");
            List<String> scanners = list.stream()
                    .map(ele -> ele.attribute("value").getValue())
                    .collect(Collectors.toList());
            configuration.setScanner(scanners);
        }

        // 组件
        List<Element> components = element.elements("component");
        List<Object> component = Components.component(components);
        configuration.setComponent(component);

        Element webNode = element.element("webConfig");
        if (webNode != null) {
            WebConfig webConfig = new WebConfig();
            // 存放jsp文件的目录
            Element webRoot = webNode.element("webappPath");
            webConfig.setWebappPath(attrValue(webRoot));
            // 端口号
            Element port = webNode.element("port");
            String value = attrValue(port);
            webConfig.setPort(Integer.parseInt(value == null ? "0" : value));
            // 编码
            Element encoding = webNode.element("encoding");
            webConfig.setEncoding(attrValue(encoding));
            // contextPath
            Element contextPath = webNode.element("contextPath");
            webConfig.setContextPath(attrValue(contextPath));
            // 前缀
            Element prefix = webNode.element("prefix");
            webConfig.setPrefix(attrValue(prefix));
            // 后缀
            Element suffix = webNode.element("suffix");
            webConfig.setSuffix(attrValue(suffix));
            // 调度器需要处理的urlPattern
            Element pattern = webNode.element("urlPattern");
            webConfig.setUrlPattern(attrValue(pattern));

            // 过滤器
            Element filterNode = webNode.element("filters");
            if (filterNode != null) {
                List<Element> list = filterNode.elements("filter");
                List<String> filters = list.stream().map(ele -> {
                    String filter = ele.attribute("class").getValue();
                    String urlPattern = ele.attribute("urlPattern").getValue();
                    return filter + "@" + urlPattern;
                }).collect(Collectors.toList());
                webConfig.setFilters(filters);
            }

            // 放行的资源
            Element exclutionsNode = webNode.element("exclutions");
            if (exclutionsNode != null) {
                List<Element> list = exclutionsNode.elements("exclution");
                List<String> exclutions = list.stream()
                        .map(ele -> ele.attribute("value").getValue())
                        .collect(Collectors.toList());
                webConfig.setExclutions(exclutions);
            }

            configuration.setWebConfig(webConfig);
        }
        return configuration;
    }

    /**
     * 获取Element属性的值
     *
     * @param element
     * @return
     */
    static String attrValue(Element element) {
        if (element == null)
            return null;
        return element.attribute(0) == null ? null : element.attribute(0).getValue();
    }
}
