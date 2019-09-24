package com.walter.orm.processor.sql;

import org.springframework.util.Assert;

import java.lang.reflect.Method;

public abstract class AbstractNamedParameterSqlProcessor {

    public abstract Object process(Class<?> targetInterface, Object proxy, Method method, Object[] args) throws Exception;

    public abstract Boolean support(Method method);

    protected boolean isCustomClass(Class<?> clz){
        Assert.notNull(clz, "input object cannot be null");
        return clz.getClassLoader() != null;
    }
}
