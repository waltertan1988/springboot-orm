package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Insert;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.InsertSqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationInsertSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        Insert insert = AnnotationUtils.getAnnotation(method, Insert.class);
        String sqlStatement = insert.statement();
        String keyField = insert.keyField();
        String dataSource = getDataSourceName(targetInterface, insert);
        return new InsertSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement, keyField);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Insert.class);
    }
}
