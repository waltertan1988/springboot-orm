package com.walter.orm.core.handler;

import com.walter.orm.core.executor.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.parser.AbstractAnnotationSqlSetParser;

import java.lang.reflect.Method;

public abstract class AbstractAnnotationSqlSetHandler {
    public Object handle(AbstractAnnotationSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Class<?> targetInterface, Method method){
        return executor.execute(parser.parse(targetInterface, method), args);
    }

    public abstract Boolean support(Method method);
}
