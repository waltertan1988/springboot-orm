package org.walter.orm.handler.annotation;

import org.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;

import java.lang.reflect.Method;

public abstract class AbstractAnnotationSqlSetHandler extends AbstractSqlSetHandler {

    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Object... extras) {
        AbstractAnnotationSqlSetParser abstractAnnotationSqlSetParser = (AbstractAnnotationSqlSetParser) parser;
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        return executor.execute(abstractAnnotationSqlSetParser.parse(targetInterface, method), wrapArgs(args, extras));
    }

    protected abstract Object[] wrapArgs(Object[] args, Object... extras);

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractAnnotationSqlSetHandler.class.isAssignableFrom(clz);
    }
}
