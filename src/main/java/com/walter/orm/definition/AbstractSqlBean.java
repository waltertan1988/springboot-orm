package com.walter.orm.definition;

import lombok.*;

import javax.sql.DataSource;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSqlBean {
    @NonNull
    private String id;
    @NonNull
    private ConfigType configType;
    @NonNull
    private String statement;
    private DataSource dataSource;

    @AllArgsConstructor
    public enum ConfigType {
        DB(0),
        XML(1);
        private int code;
    }
}
