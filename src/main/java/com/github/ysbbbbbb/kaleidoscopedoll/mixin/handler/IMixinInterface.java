package com.github.ysbbbbbb.kaleidoscopedoll.mixin.handler;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;

public interface IMixinInterface {
    static boolean applyInterfaceMixin(Class<?> targetClass) {
        return IMixinInterface.class.isAssignableFrom(targetClass);
    }

    static boolean applyInterfaceMixin(String targetClass) {
        try {
            return applyInterfaceMixin(Class.forName(targetClass, false, IMixinInterface.class.getClassLoader()));
        } catch (ClassNotFoundException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to apply mixin interface for class: {}", targetClass, e);
            return false;
        }
    }
}
