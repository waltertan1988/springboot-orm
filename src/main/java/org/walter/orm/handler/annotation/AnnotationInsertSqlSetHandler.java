package org.walter.orm.handler.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Insert;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.executor.operate.AbstractDataSourceSqlSetExecutor;
import org.walter.orm.executor.operate.InsertNamedParameterSqlSetExecutor;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import org.walter.orm.parser.annotation.AnnotationInsertSqlSetParser;

import java.lang.reflect.Method;

@Component
public class AnnotationInsertSqlSetHandler extends AbstractAnnotationSqlSetHandler{
    @Autowired
    private AnnotationInsertSqlSetParser parser;
    @Autowired
    private InsertNamedParameterSqlSetExecutor executor;

    @Override
    protected AbstractAnnotationSqlSetParser getSqlSetParser(Object... args) {
        return parser;
    }

    @Override
    protected AbstractDataSourceSqlSetExecutor getSqlSetExecutor(SqlSet sqlSet, Object... args) {
        return executor;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && method.isAnnotationPresent(Insert.class);
    }

    @Override
    protected Object[] toExecutorArgs(Object... args) {
        Method method = (Method) args[1];
        Insert insert = method.getAnnotation(Insert.class);
        String keyField = insert.keyField();
        return ArrayUtils.addAll(new Object[]{keyField}, super.toExecutorArgs(args));
    }
}
