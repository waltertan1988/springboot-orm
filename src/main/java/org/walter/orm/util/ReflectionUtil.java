package org.walter.orm.util;

import com.google.common.base.CaseFormat;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.Assert;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflectionUtil {
    private ReflectionUtil(){}

    public static String toLowerCamel(String string){
        if(null == string || (hasUnderScore(string) && hasHyphen(string))){
            throw new RuntimeException("input put string is null or with error format");
        }else if(hasUnderScore(string)){
            return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string.toLowerCase());
        }else if(hasHyphen(string)){
            return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, string.toLowerCase());
        }else if(Character.isUpperCase(string.charAt(0))){
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, string);
        }else{
            return string;
        }
    }

    public static boolean hasUnderScore(String content){
        return content.indexOf("_") > -1;
    }

    public static boolean hasHyphen(String content){
        return content.indexOf("-") > -1;
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

    public static Map<String, Object> toMap(Object object, Boolean isNullValueFieldExcluded){
        Map<String, Object> beanMap = null;

        if(object instanceof Map){
            beanMap = (Map<String, Object>) object;
        }else if(isCustomClass(object.getClass())) {
            beanMap = BeanMap.create(object);
        }else {
            throw new RuntimeException("Object type should be Map or POJO");
        }

        return beanMap.entrySet().stream()
                .filter(entry -> !isNullValueFieldExcluded || entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2)->v1));
    }
}
