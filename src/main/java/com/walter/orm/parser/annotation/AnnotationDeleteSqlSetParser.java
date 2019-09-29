package com.walter.orm.parser.annotation;

import com.walter.orm.annotation.Delete;
import com.walter.orm.core.model.AbstractSqlSet;
import com.walter.orm.sqlset.DeleteSqlSet;
import com.walter.orm.throwable.SqlSetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
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
        DataSource dataSource = applicationContext.getBean(getDataSourceName(targetInterface, delete), DataSource.class);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        return new DeleteSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement);
    }

    @Override
    public Boolean support(Class<?> clz, Method method) {
        return super.support(clz, method) && method.isAnnotationPresent(Delete.class);
    }
}
