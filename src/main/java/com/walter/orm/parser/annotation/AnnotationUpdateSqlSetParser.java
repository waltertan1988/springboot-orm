package com.walter.orm.parser.annotation;

import com.walter.orm.annotation.Update;
import com.walter.orm.core.model.AbstractSqlSet;
import com.walter.orm.sqlset.UpdateSqlSet;
import com.walter.orm.throwable.SqlSetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
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

        DataSource dataSource = applicationContext.getBean(getDataSourceName(targetInterface, update), DataSource.class);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        String sqlStatement = update.statement();

        return new UpdateSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement);
    }

    @Override
    public Boolean support(Class<?> clz, Method method) {
        return super.support(clz, method) && method.isAnnotationPresent(Update.class);
    }
}
