package com.nymph.context.method;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * jdk的invokeDynamic
 *
 * @see java.lang.invoke.MethodHandle
 */
public abstract class Dynamics {

    private static final Lookup lookup = MethodHandles.lookup();

    /**
     * 调用某个对象的方法
     *
     * @param target       被调用的对象
     * @param methodName   被调用的方法名
     * @param args         方法的参数值
     * @param returnType   方法的返回值
     * @param paramterType 方法的参数类型
     * @return 返回结果
     */
    public static Object invoke(Object target, String methodName, Object[] args, Class<?> returnType, Class<?>[] paramterType) throws Throwable {
        MethodType mt = MethodType.methodType(returnType, paramterType);
        MethodHandle mh = lookup.findVirtual(target.getClass(), methodName, mt);
        if (args == null || args.length == 0) {
            return mh.invoke(target);
        }
        Object[] argsVal = new Object[args.length + 1];
        System.arraycopy(args, 0, argsVal, 1, args.length);
        argsVal[0] = target;
        return mh.invokeWithArguments(argsVal);
    }

    /**
     * 调用指定对象的方法
     *
     * @param method 被调用的method对象
     * @param target 被调用的对象
     * @param args   方法的参数值
     * @return
     */
    public static Object invoke(Object target, Method method, Object... args) throws Throwable {
        MethodHandle mh = lookup.unreflect(method);
        if (args == null || args.length == 0) {
            return mh.invoke(target);
        }
        Object[] argsVal = new Object[args.length + 1];
        System.arraycopy(args, 0, argsVal, 1, args.length);
        argsVal[0] = target;
        return mh.invokeWithArguments(argsVal);
    }
}
