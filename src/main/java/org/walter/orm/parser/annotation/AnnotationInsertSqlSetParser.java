package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Insert;
import org.walter.orm.core.model.SqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationInsertSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public SqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        Insert insert = AnnotationUtils.getAnnotation(method, Insert.class);
        String sqlStatement = insert.statement();
        String dataSource = getDataSourceName(targetInterface, insert);
        return new SqlSet(null, SqlSet.ConfigType.ANNOTATION, SqlSet.SqlType.INSERT, dataSource, sqlStatement);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Insert.class);
    }
}
