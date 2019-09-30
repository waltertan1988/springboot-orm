package org.walter.orm.core.model;

import org.walter.orm.core.common.SupportChecker;

public abstract class AbstractSqlSetHandler implements SupportChecker {

    public Object handle(AbstractSqlSetParser parser, AbstractSqlSetExecutor executor, Object[] args, Object... extras){
        AbstractSqlSet sqlSet = parser.parse();
        return executor.execute(sqlSet, args);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractSqlSetHandler.class.isAssignableFrom(clz);
    }
}
