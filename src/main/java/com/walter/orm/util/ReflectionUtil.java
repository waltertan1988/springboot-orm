package com.walter.orm.util;

import com.google.common.base.CaseFormat;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {
    private ReflectionUtil(){}

    public static String underScoreStringToLowerCamelString(String underScoreString){
        if(underScoreString.indexOf("_") == -1){
            throw new RuntimeException();
        }
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underScoreString.toLowerCase());
    }

    public static Boolean isCustomClass(Class<?> clz){
        Assert.notNull(clz, "input object cannot be null");
        return clz.getClassLoader() != null;
    }

    public static void setBeanProperty(Object bean, String propertyName, Object value){
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, bean.getClass());
            propertyDescriptor.getWriteMethod().invoke(bean, value);
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Fail to set property %s with value %s for %s", propertyName, value.toString(), bean.getClass().getName()), e);
        }
    }

    public static <T> T getBeanProperty(Object bean, String propertyName, Class<T> returnClass){
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, bean.getClass());
            return returnClass.cast(propertyDescriptor.getReadMethod().invoke(bean, null));
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(String.format("Fail to get property %s for %s", propertyName, bean.getClass().getName()), e);
        }
    }
}
