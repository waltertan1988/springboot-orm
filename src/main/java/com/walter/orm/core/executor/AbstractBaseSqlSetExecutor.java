package com.walter.orm.core.executor;

import com.walter.orm.core.sqlset.AbstractSqlSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBaseSqlSetExecutor {
    public Object execute(AbstractSqlSet sqlSet, Object[] args){
        preExecute(sqlSet, args);
        return doExecute(sqlSet, args);
    }

    protected void preExecute(AbstractSqlSet sqlSet, Object[] args){
        log.debug("SqlSet: {}", sqlSet.toString());
        log.debug("Args: {}", args);
    }

    protected abstract Object doExecute(AbstractSqlSet sqlSet, Object[] args);
}
