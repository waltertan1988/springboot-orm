package org.walter.orm.handler.holder;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;

@Component
public class SqlSetHolderSqlSetHandler extends AbstractSqlSetHandler {
    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractSqlSetExecutor executor, Object[] args, Object... extras) {
        String id = (String) extras[0];
        return executor.execute(parser.parse(id), args);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolderSqlSetHandler.class.isAssignableFrom(clz);
    }
}
