package org.walter.orm.executor.operate;

import org.walter.orm.core.model.AbstractSqlSetExecutor;

import javax.sql.DataSource;

public abstract class AbstractDataSourceSqlSetExecutor extends AbstractSqlSetExecutor {

    protected abstract DataSource getDataSource(String dataSourceRef);

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractDataSourceSqlSetExecutor.class.isAssignableFrom(clz);
    }
}
