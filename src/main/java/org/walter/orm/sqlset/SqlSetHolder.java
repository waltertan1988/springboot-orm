package org.walter.orm.sqlset;

import org.walter.orm.core.model.AbstractSqlSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SqlSetHolder {

    private SqlSetHolder(){}

    private static List<Map<String, AbstractSqlSet>> sqlSetMapList = new ArrayList<>();

    static {
        Stream.of(AbstractSqlSet.ConfigType.values())
                .filter(configType -> !configType.equals(AbstractSqlSet.ConfigType.ANNOTATION))
                .forEach(configType -> sqlSetMapList.add(new ConcurrentHashMap<>()));
    }

    public static boolean put(AbstractSqlSet sqlSet){
        Map<String, AbstractSqlSet> map = sqlSetMapList.get(sqlSet.getConfigType().ordinal());
        if(map.containsKey(sqlSet.getId())){
            return false;
        }
        map.put(sqlSet.getId(), sqlSet);
        return true;
    }

    public static AbstractSqlSet get(AbstractSqlSet.ConfigType type, String id){
        return sqlSetMapList.get(type.ordinal()).get(id);
    }

    public static void remove(AbstractSqlSet.ConfigType type, String id){
        sqlSetMapList.get(type.ordinal()).remove(id);
    }

    public static void clear(AbstractSqlSet.ConfigType type){
        sqlSetMapList.get(type.ordinal()).clear();
    }

    public static void clear() {
        for (Map<String, AbstractSqlSet> sqlSetMap : sqlSetMapList) {
            sqlSetMap.clear();
        }
    }

    public static Integer size(AbstractSqlSet.ConfigType type){
        return sqlSetMapList.get(type.ordinal()).size();
    }

    public static Integer size() {
        int total = 0;
        for (Map<String, AbstractSqlSet> sqlSetMap : sqlSetMapList) {
            total += sqlSetMap.size();
        }
        return total;
    }

    public static Boolean isEmpty(AbstractSqlSet.ConfigType type){
        return size(type) == 0;
    }

    public static Boolean isEmpty(){
        return size() == 0;
    }

    public static List<Map<String, AbstractSqlSet>> getSqlSetMapList(){
        return sqlSetMapList;
    }
}
