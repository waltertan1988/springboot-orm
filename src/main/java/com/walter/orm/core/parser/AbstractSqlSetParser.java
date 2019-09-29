package com.walter.orm.core.parser;

import com.walter.orm.core.sqlset.AbstractSqlSet;

public interface AbstractSqlSetParser {

    default AbstractSqlSet parse(){
        return null;
    }
}
