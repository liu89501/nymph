package com.nymph.context.method;


import com.nymph.annotaion.web.DefinitionParamName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 主要用于获取方法的参数信息
 * @author LiuYang
 * @author LiangTianDong
 */
public class WrapperMethod {

    // asm获取到的方法参数名
    private String[] names;
    
    // jdk reflect api的Parameter
    private Parameter[] parameters;

    // jdk reflect api的Method
    private Method method;

    public WrapperMethod(Method method) throws Exception {
        this.method = method;
        this.parameters = method.getParameters();
        if (parameters == null || parameters.length == 0) {
            return;
        }
        DefinitionParamName name;
        if ((name = method.getAnnotation(DefinitionParamName.class)) != null) {
            this.names = name.value();
            checkParameter();
        } else {
            this.names = AsmUtils.getParamNames(method);
        }
    }

    public Method getMethod() {
        return method;
    }

    public int getParamterLength() {
        return parameters.length;
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }
    
    /**
     * 判断方法是否被指定注解标识
     * @param annotation
     * @return
     */
    public boolean isAnnotationPresent(Class<? extends Annotation> annotation) {
    	return method.isAnnotationPresent(annotation);
    }
    
    /**
     * 获取方法上的所有注解
     * @return
     */
    public Annotation[] getAnnotations() {
    	return method.getAnnotations();
    }
    
    /**
     * 获取方法的返回值类型
     * @return
     */
    public Class<?> getReturnType() {
    	return method.getReturnType();
    }

    /**
     * 获取方法参数
     * @param index
     * @return
     */
    public Parameter getParameter(int index) {
        return parameters[index];
    }
    
    /**
     * 调用方法
     * @param obj
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object obj, Object... args) throws Throwable {
    	return Dynamics.invoke(obj, method, args);
    }

    /**
     * 获取方法参数名称
     * @param index
     * @return
     */
    public String getParameterName(int index) {
        return names[index];
    }
    
    /**
     * 获取方法名
     * @return
     */
    public String getMethodName() {
    	return method.getName();
    }

    /**
     * 参数定义检查
     */
    private void checkParameter() {
        if (names.length != parameters.length) {
            throw new IllegalArgumentException(
                    String.format("参数个数异常 @DefindName定义的参数: %s个, 实际参数: %s个",
                            names.length, parameters.length));
        }
    }
}
