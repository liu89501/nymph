package com.nymph.start;

import com.nymph.utils.ClassUtils;
import org.apache.catalina.*;

import java.io.File;
import java.net.MalformedURLException;

/**
 * 生命周期监听器
 *
 * @author LIUYANG
 */
public class MainLifecycleListener implements LifecycleListener {

    private String rootPath;

    public MainLifecycleListener(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        Context ctx = (Context) event.getLifecycle();
        if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
            ctx.setConfigured(true);
            if (rootPath == null) {
                return;
            }

            rootPath = rootPath.startsWith("/") ? rootPath : "/" + rootPath;

            File file;
            if (ClassUtils.isJarLaunch()) {
                file = new File(System.getProperty("user.dir"),
                        System.getProperty("java.class.path"));
            } else {
                file = new File(ClassUtils.getSource("").getPath());
            }

            try {
                ctx.getResources().createWebResourceSet(
                        WebResourceRoot.ResourceSetType.RESOURCE_JAR,
                        "/", file.toURI().toURL(), rootPath);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
