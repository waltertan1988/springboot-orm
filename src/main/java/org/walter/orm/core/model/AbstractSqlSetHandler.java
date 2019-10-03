package org.walter.orm.core.model;

import org.walter.orm.core.common.SupportChecker;

public abstract class AbstractSqlSetHandler implements SupportChecker {

    public Object handle(Object... args){
        checkArgs(args);
        AbstractSqlSetParser parser = getSqlSetParser(args);
        SqlSet sqlSet = parser.parse(args);
        AbstractSqlSetExecutor executor = getSqlSetExecutor(sqlSet, args);
        Object[] executorArgs = toExecutorArgs(args);
        return executor.execute(sqlSet, executorArgs);
    }

    @Override
    public Boolean support(Class<?> handlerType, Object... args) {
        return AbstractSqlSetHandler.class.isAssignableFrom(handlerType);
    }

    protected abstract void checkArgs(Object... args);

    protected Object[] toExecutorArgs(Object... args){
        return args;
    }

    protected abstract AbstractSqlSetParser getSqlSetParser(Object... args);

    protected abstract AbstractSqlSetExecutor getSqlSetExecutor(SqlSet sqlSet, Object... args);
}
