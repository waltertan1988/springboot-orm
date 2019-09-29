package com.walter.orm.core.parser;

import com.walter.orm.annotation.Delete;
import com.walter.orm.core.sqlset.AbstractSqlSet;
import com.walter.orm.core.sqlset.DeleteSqlSet;
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
    public AbstractSqlSet parse(Class<?> targetInterface, Object proxy, Method method, Object[] args) {
        Delete delete = AnnotationUtils.getAnnotation(method, Delete.class);
        String sqlStatement = delete.statement();
        DataSource dataSource = applicationContext.getBean(getDataSourceName(targetInterface, delete), DataSource.class);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        return new DeleteSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement);
    }

    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(Delete.class);
    }
}
