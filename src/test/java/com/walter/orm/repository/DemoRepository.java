package com.walter.orm.repository;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetSelect;

import java.util.Date;
import java.util.Map;

@SqlSet("dataSource")
public interface DemoRepository {
    @SqlSetSelect("select now()")
    Date getCurrentDateTime();

    @SqlSetSelect("select name from department where id = :id")
    String getDepartmentName(Map<String, Object> param);

    @SqlSetSelect("select count(0) from department")
    long countAll();
}
