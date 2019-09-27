package com.walter.orm.parser;

import com.walter.orm.definition.AbstractSqlBean;

import java.util.Collection;

public abstract class AbstractOrmParser {

    protected abstract Collection<AbstractSqlBean> parse();

    protected abstract AbstractSqlBean.ConfigType supportSqlSetType();
}
