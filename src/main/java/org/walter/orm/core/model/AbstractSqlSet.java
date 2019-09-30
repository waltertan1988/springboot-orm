package org.walter.orm.core.model;

import lombok.*;

import javax.sql.DataSource;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSqlSet sqlSet = (AbstractSqlSet) o;
        return Objects.equals(id, sqlSet.id) &&
                configType == sqlSet.configType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, configType);
    }
}
