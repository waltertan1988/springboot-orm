package org.walter.orm.repository;

import org.walter.orm.annotation.SqlSet;
import org.walter.orm.annotation.Select;

import java.util.Date;
import java.util.Map;

@SqlSet("dataSource")
public interface DemoRepository {
    @Select("select now()")
    Date getCurrentDateTime();

    @Select("select name from department where id = :id")
    String getDepartmentName(Map<String, Object> param);

    @Select("select count(0) from department")
    long countAll();
}
