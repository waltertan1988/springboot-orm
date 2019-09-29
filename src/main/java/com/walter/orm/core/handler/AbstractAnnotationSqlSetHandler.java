package com.walter.orm.core.handler;

import com.walter.orm.core.executor.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.parser.AbstractAnnotationSqlSetParser;
import com.walter.orm.core.parser.AbstractSqlSetParser;

import java.lang.reflect.Method;

public abstract class AbstractAnnotationSqlSetHandler extends AbstractSqlSetHandler{

    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Object... extras) {
        AbstractAnnotationSqlSetParser abstractAnnotationSqlSetParser = (AbstractAnnotationSqlSetParser) parser;
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        return executor.execute(abstractAnnotationSqlSetParser.parse(targetInterface, method), wrapArgs(args, extras));
    }

    protected abstract Object[] wrapArgs(Object[] args, Object... extras);

    public abstract Boolean support(Method method);
}
