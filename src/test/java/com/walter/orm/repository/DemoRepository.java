package com.walter.orm.repository;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetSelect;

import java.util.Date;

@SqlSet("dataSource")
public interface DemoRepository {
    @SqlSetSelect("select now()")
    Date currentDateTime();
}
