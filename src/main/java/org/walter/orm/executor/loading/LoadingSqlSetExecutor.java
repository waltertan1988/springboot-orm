package org.walter.orm.executor.loading;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.sqlset.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

@Component
public class LoadingSqlSetExecutor extends AbstractSqlSetExecutor {
    @Override
    public Boolean support(Class<?> executorType, Object... args) {
        return LoadingSqlSetExecutor.class.isAssignableFrom(executorType);
    }

    @Override
    protected Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        Boolean success = SqlSetHolder.put(sqlSet);
        if(!success){
            throw new SqlSetException("Duplicated SqlSet id: {}", sqlSet.getId());
        }
        return null;
    }
}
