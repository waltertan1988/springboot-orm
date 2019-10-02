package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Delete;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.DeleteSqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationDeleteSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public AbstractSqlSet parse(Object... args) {
        Class<?> targetInterface = (Class<?>) args[0];
        Method method = (Method) args[1];
        Delete delete = AnnotationUtils.getAnnotation(method, Delete.class);
        String sqlStatement = delete.statement();
        String datasource = getDataSourceName(targetInterface, delete);
        return new DeleteSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, datasource, sqlStatement);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Delete.class);
    }
}
