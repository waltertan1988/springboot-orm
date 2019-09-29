package org.walter.orm.core.model;

public abstract class AbstractSqlSetHandler {

    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Object... extras){
        AbstractSqlSet sqlSet = parser.parse();
        return executor.execute(sqlSet, args);
    }
}
