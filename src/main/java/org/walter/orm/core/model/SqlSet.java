package org.walter.orm.core.model;

import lombok.*;
import org.walter.orm.core.constant.Constants;

import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SqlSet {
    protected String id;
    @NonNull
    protected ConfigType configType;
    @NonNull
    protected SqlType sqlType;
    protected String dataSource;
    @NonNull
    protected String statement;

    @AllArgsConstructor
    public enum ConfigType {
        DB(0),
        XML(1),
        ANNOTATION(2);
        private int code;
    }

    @AllArgsConstructor
    public enum SqlType {
        SELECT(Constants.SqlSet.Select.class.getSimpleName().toLowerCase()),
        DELETE(Constants.SqlSet.Delete.class.getSimpleName().toLowerCase()),
        UPDATE(Constants.SqlSet.Update.class.getSimpleName().toLowerCase()),
        INSERT(Constants.SqlSet.Insert.class.getSimpleName().toLowerCase());
        private String code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SqlSet sqlSet = (SqlSet) o;
        return Objects.equals(id, sqlSet.id) &&
                configType == sqlSet.configType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configType);
    }
}
