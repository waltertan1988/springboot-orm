package com.walter.orm.core.executor;

import java.lang.reflect.Method;

public abstract class AbstractNamedParameterSqlSetExecutor extends AbstractBaseSqlSetExecutor {

    public abstract Boolean support(Method method);
}
