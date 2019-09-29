package com.walter.orm.core.parser;

import com.walter.orm.annotation.Update;
import com.walter.orm.core.sqlset.AbstractSqlSet;
import com.walter.orm.core.sqlset.UpdateSqlSet;
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
    public boolean support(Method method) {
        return method.isAnnotationPresent(Update.class);
    }
}
