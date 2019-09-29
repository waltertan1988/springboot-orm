package com.walter.orm.parser.annotation;

import com.walter.orm.annotation.Insert;
import com.walter.orm.core.model.AbstractSqlSet;
import com.walter.orm.sqlset.InsertSqlSet;
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
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];

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
    public Boolean support(Class<?> clz, Method method) {
        return super.support(clz, method) && method.isAnnotationPresent(Insert.class);
    }
}
