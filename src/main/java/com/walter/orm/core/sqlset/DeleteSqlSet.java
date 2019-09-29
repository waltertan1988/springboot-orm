package com.walter.orm.core.sqlset;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.sql.DataSource;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class DeleteSqlSet extends AbstractSqlSet {

    public DeleteSqlSet(String id, ConfigType configType, DataSource dataSource, String statement){
        super(id, configType, dataSource, statement);
    }
}
