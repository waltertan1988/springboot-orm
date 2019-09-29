package com.walter.orm.core.sqlset;

import lombok.*;

import javax.sql.DataSource;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSqlSet {
    @NonNull
    protected String id;
    @NonNull
    protected ConfigType configType;
    protected DataSource dataSource;
    @NonNull
    protected String statement;

    @AllArgsConstructor
    public enum ConfigType {
        DB(0),
        XML(1),
        ANNOTATION(2);
        private int code;
    }
}
