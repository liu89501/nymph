package com.nymph.start;

import com.nymph.utils.ClassUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.jar.JarFile;

public class MainClassLoader extends URLClassLoader {

    public MainClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public MainClassLoader(URL[] urls) {
        super(urls);
    }

    public MainClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }


    public void start() throws IOException {
        // TODO 待实现  ClassLoader
        // TODO spring boot jar包启动的方式
        // TODO 暂时没有思路
        if (ClassUtils.isJarLaunch()) {
            File file = new File(System.getProperty("user.dir"),
                    System.getProperty("java.class.path"));
            URL url = file.toURI().toURL();
            JarFile jarFile = new JarFile(file);


            jarFile.stream()
                    .filter(jar -> jar.getName().endsWith(".jar"))
                    .forEach(jar -> {
                        System.out.println(jar);
                        try {
                            URL u = new URL(url, jar.getName());
                            System.out.println(u);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    });

        }
    }
}
