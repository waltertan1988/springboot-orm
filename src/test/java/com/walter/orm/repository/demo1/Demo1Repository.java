package com.walter.orm.repository.demo1;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetSelect;

import java.util.Map;

@SqlSet
public interface Demo1Repository {
    @SqlSetSelect(value = "select 1 from department where 1=1" +
            "<#if id??> and id = ${id}</#if>" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code = :code</#if>",
            dataSourceRef = "dataSource")
    void testMapParam(Map<String, Object> params);

    @SqlSetSelect(value = "select 1 from department where 1=1" +
            "<#if id??> and id = ${id}</#if>" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code = :code</#if>",
            dataSourceRef = "dataSource")
    void testObjectParam(Object params);
}
