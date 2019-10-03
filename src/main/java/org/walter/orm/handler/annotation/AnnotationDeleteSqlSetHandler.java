package org.walter.orm.handler.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Delete;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.executor.operate.AbstractDataSourceSqlSetExecutor;
import org.walter.orm.executor.operate.DeleteNamedParameterSqlSetExecutor;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import org.walter.orm.parser.annotation.AnnotationDeleteSqlSetParser;

import java.lang.reflect.Method;

@Component
public class AnnotationDeleteSqlSetHandler extends AbstractAnnotationSqlSetHandler{
    @Autowired
    private AnnotationDeleteSqlSetParser parser;
    @Autowired
    private DeleteNamedParameterSqlSetExecutor executor;

    @Override
    protected AbstractAnnotationSqlSetParser getSqlSetParser(Object... args) {
        return parser;
    }

    @Override
    protected AbstractDataSourceSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet, Object... args) {
        return executor;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && method.isAnnotationPresent(Delete.class);
    }
}
