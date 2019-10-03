package org.walter.orm.core.model;

import org.walter.orm.core.common.SupportChecker;

public abstract class AbstractSqlSetParser implements SupportChecker {
    @Override
    public Boolean support(Class<?> parserType, Object... args) {
        return AbstractSqlSetParser.class.isAssignableFrom(parserType);
    }

    public abstract SqlSet parse(Object... args);
}
