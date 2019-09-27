package com.walter.orm.definition;

import lombok.*;

import javax.sql.DataSource;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class SelectSqlBean extends AbstractSqlBean {
    @NonNull
    private Class<?> resultType;
    private Class<?> multiReturnElementType;

    public SelectSqlBean(String id, ConfigType configType, String statement, DataSource dataSource, Class<?> resultType, Class<?> multiReturnElementType){
        super(id, configType, statement, dataSource);
        this.resultType = resultType;
        this.multiReturnElementType = multiReturnElementType;
    }
}
