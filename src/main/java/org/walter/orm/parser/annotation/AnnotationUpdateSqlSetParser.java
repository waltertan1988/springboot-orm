package org.walter.orm.parser.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.UpdateSqlSet;

import java.lang.reflect.Method;

@Component
public class AnnotationUpdateSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];
        Update update = AnnotationUtils.getAnnotation(method, Update.class);
        String dataSource = getDataSourceName(targetInterface, update);
        String sqlStatement = update.statement();
        return new UpdateSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && method.isAnnotationPresent(Update.class);
    }
}
