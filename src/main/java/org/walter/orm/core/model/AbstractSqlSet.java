package org.walter.orm.core.model;

import lombok.*;

import javax.sql.DataSource;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSqlSet {
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
