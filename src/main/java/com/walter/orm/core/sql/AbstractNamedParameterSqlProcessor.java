package com.walter.orm.core.sql;

import java.lang.reflect.Method;

public abstract class AbstractNamedParameterSqlProcessor {

    public abstract Object process(Class<?> targetInterface, Object proxy, Method method, Object[] args) throws Exception;

    public abstract Boolean support(Method method);
}
