package com.walter.orm.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlSetHolder {

    private SqlSetHolder(){}

    private static List<Map<String, AbstractSqlBean>> sqlSetMapList = new ArrayList<>();

    static {
        for (AbstractSqlBean.ConfigType value : AbstractSqlBean.ConfigType.values()) {
            sqlSetMapList.add(new ConcurrentHashMap<>());
        }
    }

    public static void put(String id, AbstractSqlBean sqlSet){
        sqlSetMapList.get(sqlSet.getConfigType().ordinal()).put(id, sqlSet);
    }

    public static AbstractSqlBean get(AbstractSqlBean.ConfigType type, String id){
        return sqlSetMapList.get(type.ordinal()).get(id);
    }

    public static void remove(AbstractSqlBean.ConfigType type, String id){
        sqlSetMapList.get(type.ordinal()).remove(id);
    }

    public static void clear(AbstractSqlBean.ConfigType type){
        sqlSetMapList.get(type.ordinal()).clear();
    }

    public static void clear() {
        for (Map<String, AbstractSqlBean> sqlSetMap : sqlSetMapList) {
            sqlSetMap.clear();
        }
    }

    public static Integer size(AbstractSqlBean.ConfigType type){
        return sqlSetMapList.get(type.ordinal()).size();
    }

    public static Integer size() {
        int total = 0;
        for (Map<String, AbstractSqlBean> sqlSetMap : sqlSetMapList) {
            total += sqlSetMap.size();
        }
        return total;
    }

    public static Boolean isEmpty(AbstractSqlBean.ConfigType type){
        return size(type) == 0;
    }

    public static Boolean isEmpty(){
        return size() == 0;
    }

    public static List<Map<String, AbstractSqlBean>> getSqlSetMapList(){
        return sqlSetMapList;
    }
}
