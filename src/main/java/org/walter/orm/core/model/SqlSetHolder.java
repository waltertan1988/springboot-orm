package org.walter.orm.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SqlSetHolder {

    private SqlSetHolder(){}

    private static List<Map<String, SqlSet>> sqlSetMapList = new ArrayList<>();

    static {
        Stream.of(SqlSet.ConfigType.values())
                .filter(configType -> !configType.equals(SqlSet.ConfigType.ANNOTATION))
                .forEach(configType -> sqlSetMapList.add(new ConcurrentHashMap<>()));
    }

    public static boolean put(SqlSet sqlSet){
        Map<String, SqlSet> map = sqlSetMapList.get(sqlSet.getConfigType().ordinal());
        if(map.containsKey(sqlSet.getId())){
            return false;
        }
        map.put(sqlSet.getId(), sqlSet);
        return true;
    }

    public static SqlSet get(SqlSet.ConfigType type, String id){
        return sqlSetMapList.get(type.ordinal()).get(id);
    }

    public static void remove(SqlSet.ConfigType type, String id){
        sqlSetMapList.get(type.ordinal()).remove(id);
    }

    public static void clear(SqlSet.ConfigType type){
        sqlSetMapList.get(type.ordinal()).clear();
    }

    public static void clear() {
        for (Map<String, SqlSet> sqlSetMap : sqlSetMapList) {
            sqlSetMap.clear();
        }
    }

    public static Integer size(SqlSet.ConfigType type){
        return sqlSetMapList.get(type.ordinal()).size();
    }

    public static Integer size() {
        int total = 0;
        for (Map<String, SqlSet> sqlSetMap : sqlSetMapList) {
            total += sqlSetMap.size();
        }
        return total;
    }

    public static Boolean isEmpty(SqlSet.ConfigType type){
        return size(type) == 0;
    }

    public static Boolean isEmpty(){
        return size() == 0;
    }

    public static List<Map<String, SqlSet>> getSqlSetMapList(){
        return sqlSetMapList;
    }
}
