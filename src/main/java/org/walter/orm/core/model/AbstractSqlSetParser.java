package org.walter.orm.core.model;

import org.walter.orm.core.common.SupportChecker;

public abstract class AbstractSqlSetParser implements SupportChecker {

    public abstract AbstractSqlSet parse(Object... extras);

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractSqlSetParser.class.isAssignableFrom(clz);
    }
}
