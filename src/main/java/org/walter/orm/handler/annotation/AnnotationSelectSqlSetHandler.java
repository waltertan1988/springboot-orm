package org.walter.orm.handler.annotation;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Select;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.executor.operate.AbstractDataSourceSqlSetExecutor;
import org.walter.orm.executor.operate.SelectNamedParameterSqlSetExecutor;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import org.walter.orm.parser.annotation.AnnotationSelectSqlSetParser;

import java.lang.reflect.Method;

@Component
public class AnnotationSelectSqlSetHandler extends AbstractAnnotationSqlSetHandler{
    @Autowired
    private AnnotationSelectSqlSetParser parser;
    @Autowired
    private SelectNamedParameterSqlSetExecutor executor;

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
        return super.support(clz, method) && method.isAnnotationPresent(Select.class);
    }

    @Override
    protected Object[] toExecutorArgs(Object... args) {
        Method method = (Method) args[1];
        Select select = method.getAnnotation(Select.class);
        Class<?> returnType = method.getReturnType();
        Class<?> multiReturnElementType = select.multiReturnElementType();
        return ArrayUtils.addAll(new Object[]{returnType, multiReturnElementType}, super.toExecutorArgs(args));
    }
}
