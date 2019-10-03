package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Select;
import org.walter.orm.core.model.AbstractSqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationSelectSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        Select select = AnnotationUtils.getAnnotation(method, Select.class);
        String sqlStatement = select.statement();
        String dataSource = getDataSourceName(targetInterface, select);
        return new AbstractSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, AbstractSqlSet.SqlType.SELECT, dataSource, sqlStatement);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Select.class);
    }
}
