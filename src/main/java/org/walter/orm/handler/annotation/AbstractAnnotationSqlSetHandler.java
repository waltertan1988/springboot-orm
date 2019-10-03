package org.walter.orm.handler.annotation;

import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.throwable.SqlSetException;

import java.lang.reflect.Method;

public abstract class AbstractAnnotationSqlSetHandler extends AbstractSqlSetHandler {

    public static final int INVOKE_METHOD_ARGS_IDX = 2;

    @Override
    protected void checkArgs(Object... args) {
        if(args.length >= 2 && args[0] instanceof Class && args[1] instanceof Method){
            return;
        }
        throw new SqlSetException("Invalid args: %s", args);
    }

    @Override
    public Boolean support(Class<?> handlerType, Object... args) {
        return AbstractAnnotationSqlSetHandler.class.isAssignableFrom(handlerType);
    }

    @Override
    protected Object[] toExecutorArgs(Object... args) {
        if(args.length < INVOKE_METHOD_ARGS_IDX){
            throw new SqlSetException("Args count cannot be less than %s", INVOKE_METHOD_ARGS_IDX);
        }
        return (Object[]) args[INVOKE_METHOD_ARGS_IDX];
    }
}
