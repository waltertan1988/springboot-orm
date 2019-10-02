package org.walter.orm.handler.holder;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;

@Component
public class SqlSetHolderSqlSetHandler extends AbstractSqlSetHandler {
    @Override
    protected void checkArgs(Object... args) {

    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return null;
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet, Object... args) {
        return null;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolderSqlSetHandler.class.isAssignableFrom(clz);
    }
}
