package org.walter.orm.executor.loading;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.sqlset.SqlSetHolder;

@Component
public class LoadingSqlSetExecutor extends AbstractSqlSetExecutor {
    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return LoadingSqlSetExecutor.class.isAssignableFrom(clz);
    }

    @Override
    protected Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        return SqlSetHolder.put(sqlSet);
    }
}
