package org.walter.orm.handler.holder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.util.ReflectionUtil;

import java.util.Map;

@Component
public class UpdateSqlSetHolderSqlSetHandler extends AbstractSqlSetHandler {
    @Autowired
    private DefaultSqlSetHolderSqlSetHandler defaultSqlSetHolderSqlSetHandler;

    @Override
    protected Object[] toExecutorArgs(Object... args) {
        Assert.notNull(args[1], "Missing new entity");
        Assert.notNull(args[2], "Missing param");

        Map<String, Object> entity = ReflectionUtil.toMap(args[1], true);
        Map<String, Object> param = ReflectionUtil.toMap(args[2], true);

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            entity.put(Update.PARAM_PREFIX + entry.getKey(), entry.getValue());
        }

        return new Object[]{entity};
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return defaultSqlSetHolderSqlSetHandler.getSqlSetParser(args);
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(SqlSet sqlSet, Object... args) {
        return defaultSqlSetHolderSqlSetHandler.getSqlSetExecutor(sqlSet, args);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return UpdateSqlSetHolderSqlSetHandler.class.isAssignableFrom(clz);
    }

    @Override
    protected void checkArgs(Object... args) {
        defaultSqlSetHolderSqlSetHandler.checkArgs(args);
    }
}
