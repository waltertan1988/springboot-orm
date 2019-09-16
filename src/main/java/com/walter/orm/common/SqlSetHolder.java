package com.walter.orm.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlSetHolder {

    private SqlSetHolder(){}

    private static Map<String, SqlSet>[] sqlSetMaps = new ConcurrentHashMap[3];

    public static void put(String id, SqlSet sqlSet){
        sqlSetMaps[sqlSet.getType().ordinal()].put(id, sqlSet);
    }

    public static SqlSet get(SqlSet.Type type, String id){
        return sqlSetMaps[type.ordinal()].get(id);
    }

    public static void remove(SqlSet.Type type, String id){
        sqlSetMaps[type.ordinal()].remove(id);
    }

    public static void clear(SqlSet.Type type){
        sqlSetMaps[type.ordinal()].clear();
    }

    public static void clear() {
        for (Map<String, SqlSet> sqlSetMap : sqlSetMaps) {
            sqlSetMap.clear();
        }
    }

    public static Integer size(SqlSet.Type type){
        return sqlSetMaps[type.ordinal()].size();
    }

    public static Integer size() {
        int total = 0;
        for (Map<String, SqlSet> sqlSetMap : sqlSetMaps) {
            total += sqlSetMap.size();
        }
        return total;
    }

    public static Boolean isEmpty(SqlSet.Type type){
        return size(type) == 0;
    }

    public static Boolean isEmpty(){
        return size() == 0;
    }
}
