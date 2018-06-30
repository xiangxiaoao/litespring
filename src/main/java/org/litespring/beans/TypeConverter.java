package org.litespring.beans;

/**
 * 类型转换的接口
 */
public interface TypeConverter {

    <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException;
}
