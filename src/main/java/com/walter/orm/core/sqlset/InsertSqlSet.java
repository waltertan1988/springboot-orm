package com.walter.orm.core.sqlset;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.sql.DataSource;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class InsertSqlSet extends AbstractSqlSet {
    private String keyField;

    public InsertSqlSet(String id, ConfigType configType, DataSource dataSource, String statement, String keyField){
        super(id, configType, dataSource, statement);
        this.keyField = keyField;
    }
}
