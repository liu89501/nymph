package com.nymph.ioc.core;

import com.nymph.annotaion.Beans;
import com.nymph.annotaion.ConfigurationBean;
import com.nymph.config.ConfType;
import com.nymph.config.Configuration;
import com.nymph.ioc.BeansDefinition;
import com.nymph.utils.AnnoUtils;
import com.nymph.utils.ClassUtils;
import com.nymph.utils.JarUtils;
import com.nymph.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * 根据包路径扫描所有的bean
 *
 * @author NYMPH
 * @date 2017年9月26日2017年9月26日
 */
public class BeansClassScanner {
    private static final Logger LOG = LoggerFactory.getLogger(BeansClassScanner.class);
    // 文件夹分隔符
    private static final Pattern pattern = Pattern.compile("(\\\\|/)");
    // 配置
    private Configuration configuration;

    private Map<String, BeansDefinition> container;

    /**
     * 扫描所有bean
     */
    public void beansPackageScanner() throws Exception {
        loadComponent(configuration.getComponent());
        scanForJarPackages(configuration.getScanner());
        if (!ClassUtils.isJarLaunch()) {
            scanForClassPathPackages(configuration.getScanner());
        }
    }

    public BeansClassScanner(Configuration configuration, Map<String, BeansDefinition> container) {
        this.container = container;
        this.configuration = configuration;
    }

    /**
     * 加载配置文件中配置的对象
     *
     * @param components
     * @throws Exception
     */
    private void loadComponent(List<Object> components) throws Exception {
        for (Object component : components) {
            if (configuration.getConfType() == ConfType.YML) {
                String name = String.valueOf(component);
                doPutBeansDefinition(name, BeansDefinitionFactory.getSimpleDef(name));
            } else { // xml
                Class<?> aClass = component.getClass();
                String name = aClass.getName();
                BeansDefinition definition = new BeansDefinitionBuilder()
                        .setInstance(component)
                        .setType(aClass)
                        .setName(name)
                        .setPattern(BeansDefinition.SINGLETON)
                        .builder();
                container.put(name, definition);
            }
        }
    }

    /**
     * 当指定路径存在于jar包中时, 则会扫描这个jar包
     *
     * @param locations 扫描的目标路径
     * @throws Exception {@link #resolveJar}的异常
     */
    private void scanForJarPackages(List<String> locations) throws Exception {
        for (String location : locations) {
            JarFile jarFile = JarUtils.getJarFile(location);
            if (jarFile != null) {
                resolveJar(jarFile, location.replace('.', '/'));
            }
        }
    }

    /**
     * 扫描classpath下指定包路径的所有类
     *
     * @param locations 指定的路径, 可以有多个
     * @throws Exception {@link #resolverClasspath}的异常
     */
    private void scanForClassPathPackages(List<String> locations) throws Exception {
        for (String location : locations) {
            URL source = ClassUtils.getSource(location);
            if (source == null) {
                continue;
            }
            String classPath = source.getPath();
            File file = new File(classPath);
            resolverClasspath(location.replace('.', File.separatorChar), file.listFiles());
        }
    }

    /**
     * 寻找classpath下的所有class文件
     *
     * @param scan  classpath路径
     * @param files classpath下的所有文件
     * @throws Exception
     */
    private void resolverClasspath(String scan, File[] files) throws Exception {
        for (File file : files) {
            if (file.exists() && file.isDirectory()) {
                resolverClasspath(scan, file.listFiles());
            } else if (file.getName().endsWith(".class")) {
                String paths = file.getPath().replace(".class", "");
                String path = paths.substring(paths.indexOf(scan));
                String name = pattern.matcher(path).replaceAll(".");
                doPutBeansDefinition(name, BeansDefinitionFactory.getSimpleDef(name));
            }
        }
    }

    /**
     * 解析jar包中的类
     *
     * @param jarFile         表示jar文件
     * @param packageLocation 扫描的包路径
     * @throws Exception 反射时的异常
     */
    private void resolveJar(JarFile jarFile, String packageLocation) {
        jarFile.stream()
                .forEach(jarEntry -> {
                    String location = jarEntry.getName();
                    if (location.startsWith(packageLocation) && location.endsWith(".class")) {
                        String lPath = StringUtils.delete(location, ".class");
                        String name = pattern.matcher(lPath).replaceAll(".");
                        try {
                            doPutBeansDefinition(name, BeansDefinitionFactory.getSimpleDef(name));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    /**
     * 往容器中添加bean
     *
     * @param beanClassName
     */
    private void doPutBeansDefinition(String beanClassName, BeansDefinition beansDefinition) throws Exception {
        if (AnnoUtils.exist(beansDefinition.getType(), ConfigurationBean.class)) {
            configurationBeansHandler(beansDefinition.getType());
        } else {
            container.put(beanClassName, beansDefinition);
        }
    }

    /**
     * 对@ConfigurationBean的处理
     *
     * @param clazz
     * @throws Exception
     */
    private void configurationBeansHandler(Class<?> clazz) throws Exception {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Beans.class)) {
                if (Modifier.isStatic(method.getModifiers())) {
                    Class<?> type = method.getReturnType();
                    String name = type.getName();
                    BeansDefinition definition = new BeansDefinitionBuilder()
                            .setPattern(BeansDefinition.SINGLETON).setName(name)
                            .setType(type).setInstance(method.invoke(null)).builder();
                    container.put(name, definition);
                    LOG.info("ioc: in container [{}]", name);
                } else {
                    throw new IllegalArgumentException(
                            "ConfigurationBean被@Bean标识的方法请设置成静态");
                }
            }
        }
    }
}
