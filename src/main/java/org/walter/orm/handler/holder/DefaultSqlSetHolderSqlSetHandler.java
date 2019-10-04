package org.walter.orm.handler.holder;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.executor.operate.AbstractIocDataSourceSqlSetExecutor;
import org.walter.orm.parser.holder.SqlSetHolderParser;
import org.walter.orm.throwable.SqlSetException;

import java.util.Arrays;
import java.util.List;

@Component
public class DefaultSqlSetHolderSqlSetHandler extends AbstractSqlSetHandler {
    @Autowired
    private SqlSetHolderParser parser;
    @Autowired
    private List<AbstractIocDataSourceSqlSetExecutor> executorList;

    @Override
    protected void checkArgs(Object... args) {
        if(ArrayUtils.isNotEmpty(args) && args[0] instanceof String){
            return;
        }
        throw new SqlSetException("Invalid args: %s", args);
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return parser;
    }

    @Override
    protected Object[] toExecutorArgs(Object... args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(SqlSet sqlSet, Object... args) {
        return executorList.stream().filter(e -> e.support(AbstractIocDataSourceSqlSetExecutor.class, sqlSet))
                .findFirst().orElseThrow(() -> new SqlSetException("No executor found for SqlSet: %s", sqlSet));
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return DefaultSqlSetHolderSqlSetHandler.class.isAssignableFrom(clz);
    }
}
