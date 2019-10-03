package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.SqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationUpdateSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public SqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        Update update = AnnotationUtils.getAnnotation(method, Update.class);
        String dataSource = getDataSourceName(targetInterface, update);
        String sqlStatement = update.statement();
        return new SqlSet(null, SqlSet.ConfigType.ANNOTATION, SqlSet.SqlType.UPDATE, dataSource, sqlStatement);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Update.class);
    }
}
