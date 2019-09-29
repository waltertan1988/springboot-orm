package com.walter.orm.sqlset;

import com.walter.orm.core.model.AbstractSqlSet;
import lombok.*;

import javax.sql.DataSource;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class SelectSqlSet extends AbstractSqlSet {
    @NonNull
    private Class<?> resultType;
    private Class<?> multiReturnElementType;

    public SelectSqlSet(String id, ConfigType configType, DataSource dataSource, String statement, Class<?> resultType, Class<?> multiReturnElementType){
        super(id, configType, dataSource, statement);
        this.resultType = resultType;
        this.multiReturnElementType = multiReturnElementType;
    }
}
