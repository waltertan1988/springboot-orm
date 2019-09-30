package org.walter.orm.core.model;

import lombok.*;

import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractSqlSet {
    protected String id;
    @NonNull
    protected ConfigType configType;
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
