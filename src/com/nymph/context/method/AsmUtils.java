package com.nymph.context.method;

import org.objectweb.asm.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * ASM工具类, 主要用于获取方法参数名
 */
public abstract class AsmUtils {

    /**
     * 获取方法参数名
     * @param method
     * @return
     * @throws IOException
     */
    public static String[] getParamNames(Method method) throws IOException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] types = getTypes(parameterTypes);
        final String[] names = new String[parameterTypes.length];
        ClassReader reader = new ClassReader(method.getDeclaringClass().getName());
        ClassVisitor classVisitor = getClassVisitor(method.getName(), method.getModifiers(), types, names);
        reader.accept(classVisitor, 0);
        return names;
    }

    /**
     * 获取getClassVisitor对象
     * @param methodName
     * @param modifiers
     * @param methodTypes
     * @param names
     * @return
     */
    static ClassVisitor getClassVisitor(String methodName, int modifiers, Type[] methodTypes, String[] names) {
        return new ClassVisitor(Opcodes.ASM5) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                Type[] argumentTypes = Type.getArgumentTypes(desc);
                if (name.equals(methodName) && Arrays.equals(methodTypes, argumentTypes)) {
                    return new MethodVisitor(Opcodes.ASM5) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature,
                                                       Label start, Label end, int index) {
                            if (Modifier.isStatic(modifiers) && index < names.length) {
                                names[index] = name;
                            } else if (index > 0 && index <= names.length){
                                names[index - 1] = name;
                            }
                        }
                    };
                }
                return super.visitMethod(access, name, desc, signature, exceptions);
            }
        };
    }

    /**
     * 获取方法的参数类型数组
     * @param params
     * @return
     */
    static Type[] getTypes(Class<?>[] params) {
        Type[] types = new Type[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = Type.getType(params[i]);
        }
        return types;
    }
}
