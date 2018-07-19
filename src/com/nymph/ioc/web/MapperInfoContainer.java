/**
 *
 */
package com.nymph.ioc.web;

import com.nymph.annotaion.Http;
import com.nymph.annotaion.web.Method;
import com.nymph.ioc.BeansDefinition;
import com.nymph.utils.AnnoUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 关于web层的bean的一些处理, 主要是将一个完整的url映射到具体的一个方法, 这样可以加快访问的速度, 直接通过浏览器
 * 的url地址的字符串就能获取到对应的类和方法
 *
 * @author NYMPH
 * @date 2017年9月26日2017年9月26日
 */
public class MapperInfoContainer implements Runnable {

    private final Map<String, MapperInfo> httpMap = new HashMap<>(256);

    private final Map<String, BeansDefinition> container;

    public MapperInfoContainer(Map<String, BeansDefinition> container) {
        this.container = container;
    }

    /**
     * 对拥有@HTTP注解的bean进行处理主要是将@HTTP注解的value和@Request系列注解(@GET这种)
     * 的value拼接成一个完整的表示url地址的字符串, 然后存入map中, 这样当一个请求访问过来
     * 时只需要拿到这个请求的uri就可以直接获取到对应的Class和Method,如果用@UrlHolder
     * 注解这种占位符形式的url的话就只有遍历来寻找是否存在请求所映射的类了
     */
    public void filterAlsoSave() throws Exception {
        for (Entry<String, BeansDefinition> entry : container.entrySet()) {
            BeansDefinition definition = entry.getValue();
            Class<?> type = definition.getType();
            Http http;
            // 没有@Http注解的bean则不进行处理
            if ((http = type.getAnnotation(Http.class)) == null)
                continue;

            for (java.lang.reflect.Method method : type.getDeclaredMethods()) {
                Annotation[] annotations = method.getAnnotations();
                Annotation request;
                if ((request = AnnoUtils.get(annotations, Method.class)) != null) {
                    method.setAccessible(true);
                    String url = joinUrl(http.value(), request);
                    httpMap.put(url, new MapperInfo(entry.getKey(), method));
                }
            }
        }
    }

    /**
     * 将web映射对象的@HTTP注解和 方法的请求注解(@GET,@POST 等) 的值拼接成一个完整的url
     *
     * @param spaceVal -@HTTP注解value方法的值
     * @param method   方法上的请求方式注解(@GET或其他)
     * @return 拼接好的url
     */
    private String joinUrl(String spaceVal, Annotation method) throws Exception {
        String methodVal = AnnoUtils.getStringOfValueMethod(method);
        return ("/".equals(spaceVal) || spaceVal.equals("") ? "" :
                spaceVal.startsWith("/") ? spaceVal : "/" + spaceVal)
                + (!methodVal.startsWith("/") ? "/" + methodVal : methodVal);
    }

    @Override
    public String toString() {
        return String.valueOf(httpMap);
    }

    public MapperInfo getMapperInfo(String url) {
        return httpMap.get(url);
    }

    public Iterator<Entry<String, MapperInfo>> getIterator() {
        return httpMap.entrySet().iterator();
    }

    @Override
    public void run() {
        try {
            filterAlsoSave();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 容器内存放的对象
     */
    public class MapperInfo implements Cloneable {
        // web层映射类的类名(类的全名)
        private String name;
        // 路径对应的方法
        private java.lang.reflect.Method method;
        // @PathField注解的value值
        private Map<String, String> placeHolder;

        public MapperInfo(String name, java.lang.reflect.Method method) {
            this.name = name;
            this.method = method;
        }

        public MapperInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public java.lang.reflect.Method getMethod() {
            return method;
        }

        public void setMethod(java.lang.reflect.Method method) {
            this.method = method;
        }

        public Map<String, String> getPlaceHolder() {
            return placeHolder;
        }

        public MapperInfo initialize(Map<String, String> placeHolder) throws CloneNotSupportedException {
            MapperInfo info = (MapperInfo) this.clone();
            info.placeHolder = placeHolder;
            return info;
        }
    }
}
