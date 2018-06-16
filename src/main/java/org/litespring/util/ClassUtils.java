package org.litespring.util;

public abstract class ClassUtils {
    //优先获取当前线程的classLoader,若没空，则获取当前类的classLoader
    //如若再获取不到，则获取系统的classLoader
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();

            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return cl;
    }
}
