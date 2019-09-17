package com.walter.orm.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlSetHolder {

    private SqlSetHolder(){}

    private static List<Map<String, SqlSet>> sqlSetMaps = new ArrayList<>();

    static {
        for (SqlSet.Type value : SqlSet.Type.values()) {
            sqlSetMaps.add(new ConcurrentHashMap<>());
        }
    }

    public static void put(String id, SqlSet sqlSet){
        sqlSetMaps.get(sqlSet.getType().ordinal()).put(id, sqlSet);
    }

    public static SqlSet get(SqlSet.Type type, String id){
        return sqlSetMaps.get(type.ordinal()).get(id);
    }

    public static void remove(SqlSet.Type type, String id){
        sqlSetMaps.get(type.ordinal()).remove(id);
    }

    public static void clear(SqlSet.Type type){
        sqlSetMaps.get(type.ordinal()).clear();
    }

    public static void clear() {
        for (Map<String, SqlSet> sqlSetMap : sqlSetMaps) {
            sqlSetMap.clear();
        }
    }

    public static Integer size(SqlSet.Type type){
        return sqlSetMaps.get(type.ordinal()).size();
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
