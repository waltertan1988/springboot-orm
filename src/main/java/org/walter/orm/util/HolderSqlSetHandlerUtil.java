package org.walter.orm.util;

import org.walter.orm.handler.holder.DefaultSqlSetHolderSqlSetHandler;
import org.walter.orm.handler.holder.UpdateSqlSetHolderSqlSetHandler;

import java.util.Collection;
import java.util.Map;

public class HolderSqlSetHandlerUtil {

    public static <E> Collection<E> selectMany(String sqlSetId, Class<E> multiReturnElementType, Object pojoCondition){
        return  (Collection) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, Collection.class, multiReturnElementType, pojoCondition);
    }

    public static Collection<Map<String, Object>> selectMany(String sqlSetId, Object pojoCondition){
        return (Collection) selectMany(sqlSetId, Map.class, pojoCondition);
    }

    public static <E> Collection<E> selectMany(String sqlSetId, Class<E> multiReturnElementType, Map<String, Object> mapCondition){
        return (Collection) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, Collection.class, multiReturnElementType, mapCondition);
    }

    public static Collection<Map<String, Object>> selectMany(String sqlSetId, Map<String, Object> mapCondition){
        return (Collection) selectMany(sqlSetId, Map.class, mapCondition);
    }

    public static Collection<Map<String, Object>> selectMany(String sqlSetId){
        return (Collection) selectMany(sqlSetId, Map.class, null);
    }

    public static <T> T selectOne(String sqlSetId, Class<T> returnType, Object pojoCondition){
        return (T) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, returnType, null, pojoCondition);
    }

    public static Map<String, Object> selectOne(String sqlSetId, Object pojoCondition){
        return (Map) selectOne(sqlSetId, Map.class, pojoCondition);
    }

    public static <T> T selectOne(String sqlSetId, Class<T> returnType, Map<String, Object> mapCondition){
        return (T) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, returnType, null, mapCondition);
    }

    public static Map<String, Object> selectOne(String sqlSetId, Map<String, Object> mapCondition){
        return (Map) selectOne(sqlSetId, Map.class, mapCondition);
    }

    public static <T> T selectOne(String sqlSetId, Class<T> returnType){
        return selectOne(sqlSetId, returnType, null);
    }

    public static Map<String, Object> selectOne(String sqlSetId){
        return selectOne(sqlSetId, Map.class);
    }

    public static Integer save(String sqlSetId, Object object, String keyField){
        return (Integer) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, keyField, object);
    }

    public static Integer save(String sqlSetId, Object object){
        return save(sqlSetId, object, null);
    }

    public static Integer save(String sqlSetId, Map mapObject, String keyField){
        return (Integer) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, keyField, mapObject);
    }

    public static Integer save(String sqlSetId, Map mapObject){
        return save(sqlSetId, mapObject, null);
    }

    public static Integer delete(String sqlSetId, Object pojoCondition){
        return (Integer) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, pojoCondition);
    }

    public static Integer delete(String sqlSetId, Map<String, Object> mapCondition){
        return (Integer) ApplicationContextHolder.applicationContext.getBean(DefaultSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, mapCondition);
    }

    public static Integer update(String sqlSetId, Object newValueObject, Object condition){
        return (Integer) ApplicationContextHolder.applicationContext.getBean(UpdateSqlSetHolderSqlSetHandler.class)
                .handle(sqlSetId, newValueObject, condition);
    }
}
