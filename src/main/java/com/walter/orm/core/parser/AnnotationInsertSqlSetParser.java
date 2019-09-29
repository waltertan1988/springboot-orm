package com.walter.orm.core.parser;

import com.walter.orm.annotation.Insert;
import com.walter.orm.core.sqlset.AbstractSqlSet;
import com.walter.orm.core.sqlset.InsertSqlSet;
import com.walter.orm.throwable.SqlSetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;

@Component
public class AnnotationInsertSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public AbstractSqlSet parse(Class<?> targetInterface, Object proxy, Method method, Object[] args) {
        Insert insert = AnnotationUtils.getAnnotation(method, Insert.class);
        String sqlStatement = insert.statement();
        String keyField = insert.keyField();
        DataSource dataSource = applicationContext.getBean(getDataSourceName(targetInterface, insert), DataSource.class);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        return new InsertSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement, keyField);
    }

    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(Insert.class);
    }
}
