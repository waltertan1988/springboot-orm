package com.walter.orm.core.parser;

import com.walter.orm.core.sqlset.AbstractSqlSet;

public abstract class AbstractSqlSetParser {

    public abstract AbstractSqlSet parse(Object... extras);
}
