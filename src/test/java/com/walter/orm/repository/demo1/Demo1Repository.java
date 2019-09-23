package com.walter.orm.repository.demo1;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetSelect;

import java.util.List;
import java.util.Map;

@SqlSet
public interface Demo1Repository {
    @SqlSetSelect(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource")
    List<?> listMapByObject(Object param);

    @SqlSetSelect(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource", multiReturnElementType = Demo1Domain.class)
    List<?> listObjectByMap(Map<String, Object> params);

    @SqlSetSelect(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Demo1Domain getObjectByObject(Demo1Domain param);

    @SqlSetSelect(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Demo1Domain getObjectByMap(Map<String, Object> params);
}
