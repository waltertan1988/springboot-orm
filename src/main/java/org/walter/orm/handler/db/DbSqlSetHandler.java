package org.walter.orm.handler.db;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.executor.loading.LoadingSqlSetExecutor;
import org.walter.orm.parser.db.DbSqlSetParser;
import org.walter.orm.throwable.SqlSetException;

@Component
public class DbSqlSetHandler extends AbstractSqlSetHandler {
    @Autowired
    private DbSqlSetParser dbSqlSetParser;
    @Autowired
    private LoadingSqlSetExecutor loadingSqlSetExecutor;

    @Override
    protected void checkArgs(Object... args) {
        if(ArrayUtils.isNotEmpty(args) && args.length == 1 && (args[0] instanceof SqlSet)){
            return;
        }
        throw new SqlSetException("Invalid args: %s", args);
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return dbSqlSetParser;
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(SqlSet sqlSet, Object... args) {
        return loadingSqlSetExecutor;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return DbSqlSetHandler.class.isAssignableFrom(clz);
    }
}
