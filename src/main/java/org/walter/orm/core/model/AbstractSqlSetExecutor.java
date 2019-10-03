package org.walter.orm.core.model;

import lombok.extern.slf4j.Slf4j;
import org.walter.orm.core.common.SupportChecker;

@Slf4j
public abstract class AbstractSqlSetExecutor implements SupportChecker {
    public Object execute(SqlSet sqlSet, Object[] args){
        preExecute(sqlSet, args);
        return doExecute(sqlSet, args);
    }

    protected void preExecute(SqlSet sqlSet, Object[] args){
        log.debug("id: {}, datasource: {}", sqlSet.getId(), sqlSet.getDataSource());
        log.debug("statement: {}", sqlSet.getStatement());
        log.debug("args: {}", args);
    }

    @Override
    public Boolean support(Class<?> executorType, Object... args) {
        return AbstractSqlSetExecutor.class.isAssignableFrom(executorType);
    }

    protected abstract Object doExecute(SqlSet sqlSet, Object[] args);
}
