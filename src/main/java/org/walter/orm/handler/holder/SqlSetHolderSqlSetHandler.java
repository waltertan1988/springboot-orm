package org.walter.orm.handler.holder;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.executor.operate.AbstractIocDataSourceSqlSetExecutor;
import org.walter.orm.parser.xml.operate.OperateXmlSqlSetParser;
import org.walter.orm.throwable.SqlSetException;

import java.util.List;

@Component
public class SqlSetHolderSqlSetHandler extends AbstractSqlSetHandler {
    @Autowired
    private OperateXmlSqlSetParser parser;
    @Autowired
    private List<AbstractIocDataSourceSqlSetExecutor> executorList;

    @Override
    protected void checkArgs(Object... args) {
        if(ArrayUtils.isNotEmpty(args) && args.length == 2 && args[0] instanceof String){
            return;
        }
        throw new SqlSetException("Invalid args: ?", args);
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return parser;
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet, Object... args) {
        return executorList.stream().filter(e -> e.support(AbstractIocDataSourceSqlSetExecutor.class, sqlSet))
                .findFirst().orElseThrow(() -> new SqlSetException("No executor found for SqlSet: ?", sqlSet));
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolderSqlSetHandler.class.isAssignableFrom(clz);
    }
}
