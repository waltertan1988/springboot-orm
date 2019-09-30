package org.walter.orm.parser.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Delete;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.DeleteSqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationDeleteSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        Delete delete = AnnotationUtils.getAnnotation(method, Delete.class);
        String sqlStatement = delete.statement();
        String datasource = getDataSourceName(targetInterface, delete);
        return new DeleteSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, datasource, sqlStatement);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && method.isAnnotationPresent(Delete.class);
    }
}
