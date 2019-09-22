package com.walter.orm.repository;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetStatement;

import java.util.Date;

@SqlSet("dataSource")
public interface Demo1Repository {
    @SqlSetStatement("select now()")
    Date currentDateTime();
}
