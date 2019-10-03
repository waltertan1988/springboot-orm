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
        log.debug("id: {}", sqlSet.getId());
        log.debug("datasource: {}", sqlSet.getDataSource());
        log.debug("statement: {}", sqlSet.getStatement());
        for (int i = 0; i < args.length; i++) {
            log.debug("args[{}]: {}", i, args[i]);
        }
    }

    @Override
    public Boolean support(Class<?> executorType, Object... args) {
        return AbstractSqlSetExecutor.class.isAssignableFrom(executorType);
    }

    protected abstract Object doExecute(SqlSet sqlSet, Object[] args);
}
