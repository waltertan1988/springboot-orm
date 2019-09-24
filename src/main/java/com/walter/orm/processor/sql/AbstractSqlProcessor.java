package com.walter.orm.processor.sql;

import org.springframework.util.Assert;

import javax.sql.DataSource;

public abstract class AbstractSqlProcessor {

    public abstract Object process(DataSource dataSource, String preparedSqlStatement, Object param, Class<?> returnType, Class<?> multiReturnElementType) throws Exception;

    protected boolean isCustomClass(Class<?> clz){
        Assert.notNull(clz, "input object cannot be null");
        return clz.getClassLoader() != null;
    }
}
