package org.walter.orm.core.model;

import lombok.extern.slf4j.Slf4j;
import org.walter.orm.core.common.SupportChecker;

import javax.sql.DataSource;

@Slf4j
public abstract class AbstractBaseSqlSetExecutor implements SupportChecker {
    public Object execute(AbstractSqlSet sqlSet, Object[] args){
        preExecute(sqlSet, args);
        return doExecute(sqlSet, args);
    }

    protected void preExecute(AbstractSqlSet sqlSet, Object[] args){
        log.debug("id: {}, datasource: {}", sqlSet.getId(), sqlSet.getDataSource());
        log.debug("statement: {}", sqlSet.getStatement());
        log.debug("args: {}", args);
    }

    protected abstract Object doExecute(AbstractSqlSet sqlSet, Object[] args);

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractBaseSqlSetExecutor.class.isAssignableFrom(clz);
    }

    protected abstract DataSource getDataSource(String dataSourceRef);
}
