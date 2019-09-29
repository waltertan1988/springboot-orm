package com.walter.orm.core.handler;

import com.walter.orm.core.executor.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.parser.AbstractSqlSetParser;
import com.walter.orm.core.sqlset.AbstractSqlSet;

public abstract class AbstractSqlSetHandler {

    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args){
        AbstractSqlSet sqlSet = parser.parse();
        return executor.execute(sqlSet, args);
    }
}
