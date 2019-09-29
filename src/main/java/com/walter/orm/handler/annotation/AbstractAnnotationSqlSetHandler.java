package com.walter.orm.handler.annotation;

import com.walter.orm.core.common.SupportChecker;
import com.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.model.AbstractSqlSetHandler;
import com.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import com.walter.orm.core.model.AbstractSqlSetParser;

import java.lang.reflect.Method;

public abstract class AbstractAnnotationSqlSetHandler extends AbstractSqlSetHandler implements SupportChecker {

    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Object... extras) {
        AbstractAnnotationSqlSetParser abstractAnnotationSqlSetParser = (AbstractAnnotationSqlSetParser) parser;
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        return executor.execute(abstractAnnotationSqlSetParser.parse(targetInterface, method), wrapArgs(args, extras));
    }

    protected abstract Object[] wrapArgs(Object[] args, Object... extras);

    @Override
    public Boolean support(Class<?> clz, Method method) {
        return AbstractAnnotationSqlSetHandler.class.isAssignableFrom(clz);
    }
}
