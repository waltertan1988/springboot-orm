package org.walter.orm.handler.annotation;

import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.executor.operate.AbstractDataSourceSqlSetExecutor;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import org.walter.orm.throwable.SqlSetException;

import java.lang.reflect.Method;

public abstract class AbstractAnnotationSqlSetHandler extends AbstractSqlSetHandler {

    public static final int INVOKE_METHOD_ARGS_IDX = 2;

    @Override
    protected void checkArgs(Object... args) {
        if(args.length >= 2 && args[0] instanceof Class && args[1] instanceof Method){
            return;
        }
        throw new SqlSetException("Invalid args: {}", args);
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        Method method = (Method) args[1];
        return getSqlSetParser(method);
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet, Object... args) {
        return getSqlSetExecutor(sqlSet);
    }

    @Override
    public Boolean support(Class<?> handlerType, Object... args) {
        return AbstractAnnotationSqlSetHandler.class.isAssignableFrom(handlerType);
    }

    @Override
    protected Object[] toExecutorArgs(Object... args) {
        if(args.length < INVOKE_METHOD_ARGS_IDX){
            throw new SqlSetException("Args count cannot be less than {}", INVOKE_METHOD_ARGS_IDX);
        }
        return (Object[]) args[INVOKE_METHOD_ARGS_IDX];
    }

    protected abstract AbstractAnnotationSqlSetParser getSqlSetParser(Method method);

    protected abstract AbstractDataSourceSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet);
}
