package com.walter.orm.repository.demo1;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.Insert;
import com.walter.orm.annotation.Select;

import java.util.List;
import java.util.Map;

@SqlSet
public interface Demo1Repository {
    @Select(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource")
    List<?> listMapByObject(Object param);

    @Select(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource", multiReturnElementType = Demo1Domain.class)
    List<?> listObjectByMap(Map<String, Object> params);

    @Select(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Demo1Domain getObjectByObject(Demo1Domain param);

    @Select(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Demo1Domain getObjectByMap(Map<String, Object> params);

    @Insert(value = "insert into department(code, name) values (:code, :name)", keyField = "id", dataSourceRef = "dataSource")
    int saveObject(Demo1Domain demo1Domain);

    @Insert(value = "insert into department(code, name) values (:code, :name)", keyField = "id", dataSourceRef = "dataSource")
    int saveMap(Map<String, Object> map);
}
