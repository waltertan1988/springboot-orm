package com.walter.orm.core.handler;

import com.walter.orm.core.executor.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.parser.AbstractSqlSetParser;

public abstract class AbstractSqlSetHandler {

    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args){
        return executor.execute(parser.parse(), args);
    }
}
