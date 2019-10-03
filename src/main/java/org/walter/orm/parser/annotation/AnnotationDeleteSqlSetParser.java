package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Delete;
import org.walter.orm.core.model.SqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationDeleteSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public SqlSet parse(Object... args) {
        Class<?> targetInterface = (Class<?>) args[0];
        Method method = (Method) args[1];
        Delete delete = AnnotationUtils.getAnnotation(method, Delete.class);
        String sqlStatement = delete.statement();
        String datasource = getDataSourceName(targetInterface, delete);
        return new SqlSet(null, SqlSet.ConfigType.ANNOTATION, SqlSet.SqlType.DELETE, datasource, sqlStatement);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Delete.class);
    }
}
