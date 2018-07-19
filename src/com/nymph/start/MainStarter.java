package com.nymph.start;

import com.nymph.config.ConfRead;
import com.nymph.config.Configuration;
import com.nymph.config.WebConfig;
import com.nymph.context.WebApplicationInitialization;
import com.nymph.ioc.BeansHandler;
import com.nymph.utils.ClassUtils;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 内嵌tomcat,  实现main方法启动应用
 *
 * @author LiuYang
 * @author LiangTianDong
 * @date 2017年9月17日上午2:33:52
 */
public class MainStarter extends WebApplicationInitialization {
    // Tomcat实例
    private final Tomcat tomcat = new Tomcat();
    // 配置文件的匹配规则
    private static final Pattern pattern = Pattern.compile("^nymph.*\\.(xml|yml)$");
    // log.
    private static final Logger LOG = LoggerFactory.getLogger(MainStarter.class);
    // 默认web配置
    private Configuration defaultConf;

    public MainStarter() {
        defaultConf = new Configuration();
        defaultConf.setWebConfig(new WebConfig(8080, "", "/", "UTF-8"));
    }

    /**
     * 启动内嵌TOMCAT
     *
     * @param clazz
     */
    public static void start(Class<?> clazz) {
        try {
            new MainStarter().launch(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化配置并启动
     *
     * @throws Exception
     */
    private void launch(Class<?> clazz) throws Exception {
        Starter starter = clazz.getAnnotation(Starter.class);
        if (starter == null) {
            throw new IllegalArgumentException(
                    "Starter class require use @Starter annotation");
        }
        // 初始化配置
        initConfiguration(starter, clazz);
        // 初始化bean工厂
        initBeansFactory(starter);
        // 获取Configuration中的web配置
        config = getConfiguration().getWebConfig();
        // 端口号 和 catalina home
        tomcat.setPort(config.getPort());
        tomcat.setBaseDir(System.getProperty("user.dir"));
        // tomcat上下文
        Context context = initContext();
        loadFilters(context);
        loadServlets(context);
        initBaseConfig();

        tomcat.getHost().addChild(context);
        tomcat.start();
        tomcat.getServer().await();
    }

    /**
     * 初始化配置
     *
     * @param starter
     * @param clazz
     */
    protected void initConfiguration(Starter starter, Class<?> clazz) throws IOException {

        String[] configurations = starter.configFiles();
        String[] packageScanner = starter.packageScans();

        Configuration conf;
        if (ClassUtils.isJarLaunch()) {
            if (configurations.length == 0) {
                conf = ConfRead.readConfStream(defaultConfStream());
            } else {
                conf = ConfRead.readConfStream(getJarResource(configurations));
            }
        } else {
            if (configurations.length == 0) {
                conf = ConfRead.readConfLocation(defaultConf());
            } else {
                conf = ConfRead.readConf(configurations);
            }
        }


        if (conf == null) {
            setConfiguration(defaultConf);
        } else {
            defaultConf = null;
            setConfiguration(conf);
        }


        if (packageScanner.length == 0) {
            /**
             * 未指定ioc的扫描路劲时, 默认扫描启动类的包路径
             */
            String packageName = clazz.getPackage().getName();
            getConfiguration().setScanner(Arrays.asList(packageName));
        } else {
            getConfiguration().setScanner(Arrays.asList(packageScanner));
        }
    }

    /**
     * 初始化bean工厂
     *
     * @param starter
     */
    protected void initBeansFactory(Starter starter) throws Exception {
        getBeansFactory().setConfiguration(getConfiguration());
        Class<?> beanRound = starter.beanRound();
        if (BeansHandler.class.isAssignableFrom(beanRound)) {
            // 实例化bean处理器
            BeansHandler handler = ClassUtils.newInstance(beanRound);
            getBeansFactory().setBeansHandler(handler);
        }
        getBeansFactory().register();
    }

    /**
     * 初始化tomcat的上下文
     *
     * @return
     */
    protected Context initContext() {
        Context context = new StandardContext();
        context.setPath(config.getContextPath());
        String rootPath = config.getWebappPath();
        if (rootPath == null) {
            if (ClassUtils.getSource("src/main/webapp") != null) {
                rootPath = "/src/main/webapp";
            }
            if (ClassUtils.getSource("WebContent") != null) {
                rootPath = "/WebContent";
            }
            if (ClassUtils.getSource("WebRoot") != null) {
                rootPath = "/WebRoot";
            }
            if (ClassUtils.getSource("template") != null) {
                rootPath = "/template";
            }

        }
        context.addLifecycleListener(new MainLifecycleListener(rootPath));
        return context;
    }

    /**
     * 获取classpath下的找到的xml和yml配置文件(文件开头名称为nymph的)
     *
     * @return 所有找到的文件名字
     */
    protected List<String> defaultConf() {
        File file = new File(ClassUtils.getSource("").getPath());
        File[] files = file.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files)
                .filter(f -> {
                    String name = f.getName();
                    Matcher matcher = pattern.matcher(name);
                    return f.isFile() && matcher.matches();
                })
                .map(f -> f.toString())
                .collect(Collectors.toList());
    }

    /**
     * 获取jar根目录下找到的xml和yml配置文件(文件开头名称为nymph的)
     *
     * @return 所有找到的文件名字
     */
    protected List<InputStream> defaultConfStream() throws IOException {
        File file = new File(System.getProperty("user.dir"),
                System.getProperty("java.class.path"));

        JarFile jarFile = new JarFile(file);
        return jarFile.stream()
                .filter(jar -> {
                    Matcher matcher = pattern.matcher(jar.getName());
                    return !jar.isDirectory() && matcher.matches();
                }).map(jar -> {
                    try {
                        return jarFile.getInputStream(jar);
                    } catch (IOException e) {
                        return null;
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 获取jar包中的资源文件
     *
     * @param resource
     * @return
     * @throws IOException
     */
    protected List<InputStream> getJarResource(String... resource) throws IOException {
        File file = new File(System.getProperty("user.dir"),
                System.getProperty("java.class.path"));
        JarFile jarFile = new JarFile(file);
        return Arrays.stream(resource)
                .map(r -> {
                    try {
                        return jarFile.getInputStream(jarFile.getEntry(r));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 加载一些必须的servlet
     *
     * @param context tomcat的上下文
     */
    protected void loadServlets(Context context) {
        Tomcat.initWebappDefaults(context);
        for (String exclude : config.getExclutions()) {
            /** 忽略的servlet url */
            context.addServletMappingDecoded(exclude, "default");
        }
        Wrapper dispatcher = Tomcat.addServlet(
                context, "nymph", CORE_REQUEST_DISPATCHER);
        dispatcher.addMapping(config.getUrlPattern());
        dispatcher.setAsyncSupported(true);
    }

    /**
     * 加载过滤器
     *
     * @param context tomcat的上下文对象
     */
    protected void loadFilters(Context context) {
        // 编码过滤器
        FilterDef defaultFilter = filterDef(DEFAULT_ENCODING_FILTER);
        defaultFilter.addInitParameter("encoding", config.getEncoding());
        FilterMap defaultMap = filterMap(DEFAULT_ENCODING_FILTER, "/*");
        context.addFilterDef(defaultFilter);
        context.addFilterMap(defaultMap);
        // 自定义的过滤器链
        for (String filter : config.getFilters()) {
            String urlPattern = "/*";
            if (filter.indexOf("@") > 0) {
                String[] split = filter.split("@");
                urlPattern = split[1];
                filter = split[0];
            }
            FilterDef filterDef = filterDef(filter);
            FilterMap filterMap = filterMap(filter, urlPattern);
            context.addFilterDef(filterDef);
            context.addFilterMap(filterMap);
        }
    }

    /**
     * 定义一个过滤器
     *
     * @param className 过滤器的全路径
     * @return
     */
    protected FilterDef filterDef(String className) {
        FilterDef def = new FilterDef();
        def.setAsyncSupported("true");
        def.setFilterClass(className);
        def.setFilterName(className);
        return def;
    }

    /**
     * 设置过滤器的url映射
     *
     * @param name       要映射的过滤器的名字
     * @param urlPattern 要过滤的url
     * @return
     */
    protected FilterMap filterMap(String name, String urlPattern) {
        FilterMap map = new FilterMap();
        map.addURLPattern(urlPattern);
        map.setFilterName(name);
        return map;
    }

}
