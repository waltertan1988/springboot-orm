package org.walter.orm.repository.demo1;

import org.walter.orm.annotation.*;

import java.util.List;
import java.util.Map;

@SqlSet
public interface Demo1Repository {
    @Select(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource")
    List<?> listMapByObject(Object param);

    @Select(statement = "select name from department " +
            "where 1=1 " +
            "and code in (:codes)",
            dataSourceRef = "dataSource", multiReturnElementType = String.class)
    List<String> listNameByCodeIn(Map<String, Object> params);

    @Select(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource", multiReturnElementType = Demo1Domain.class)
    List<Demo1Domain> listObjectByMap(Map<String, Object> params);

    @Select(statement = "select * from department" +
            "where 1=1 " +
            "and code in (:codes)", dataSourceRef = "dataSource")
    List<?> listMapByCodeIn(Map<String, Object> params);

    @Select(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Map<String, Object> getMapByObject(Demo1Domain param);

    @Select(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Demo1Domain getObjectByMap(Map<String, Object> params);

    @Insert(value = "insert into department(code, name) values (:code, :name)", keyField = "id", dataSourceRef = "dataSource")
    int saveObject(Demo1Domain demo1Domain);

    @Insert(value = "insert into department(code, name) values (:code, :name)", keyField = "id", dataSourceRef = "dataSource")
    int saveMap(Map<String, Object> map);

    @Delete(value = "delete from department where name like '%${name}%'", dataSourceRef = "dataSource")
    void deleteByObject(Demo1Domain demo1Domain);

    @Delete(value = "delete from department where id > :id", dataSourceRef = "dataSource")
    int deleteByMap(Map<String, Object> map);

    @Update(value = "update department set code = :code where code = :_code", dataSourceRef = "dataSource")
    int updateObjectByObject(Demo1Domain newDomain, @Param Demo1Domain param);

    @Update(value = "update department set " +
            "name = <#if name == '_NULL'>null<#else>:name</#if> " +
            "where name <#if _name?? && _name == '_NULL'>is null<#else>=:_name</#if>"
            , dataSourceRef = "dataSource")
    int updateObjectByObjectWithNull(Demo1Domain newDomain, @Param Demo1Domain param);
}
