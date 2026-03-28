package com.github.ysbbbbbb.kaleidoscopedoll.mixin.handler;

public interface IMixinInterface {

    static boolean applyInterfaceMixin(Class<?> targetClass) {
        return IMixinInterface.class.isAssignableFrom(targetClass);
    }

    static boolean applyInterfaceMixin(String targetClass) {
        try {
            return applyInterfaceMixin(Class.forName(targetClass, false, IMixinInterface.class.getClassLoader()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}
