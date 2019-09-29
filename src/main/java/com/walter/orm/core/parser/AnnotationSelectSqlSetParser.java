package com.walter.orm.core.parser;

import com.walter.orm.annotation.Select;
import com.walter.orm.core.sqlset.AbstractSqlSet;
import com.walter.orm.core.sqlset.SelectSqlSet;
import com.walter.orm.throwable.SqlSetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Collection;

@Component
public class AnnotationSelectSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public AbstractSqlSet parse(Class<?> targetInterface, Object proxy, Method method, Object[] args) {
        Select select = AnnotationUtils.getAnnotation(method, Select.class);
        String sqlStatement = select.statement();
        DataSource dataSource = applicationContext.getBean(getDataSourceName(targetInterface, select), DataSource.class);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        Class<?> returnType = method.getReturnType();

        Class<?> multiReturnElementType = null;
        if(Collection.class.isAssignableFrom(returnType)){
            multiReturnElementType = select.multiReturnElementType();
        }

        return new SelectSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement, returnType, multiReturnElementType);
    }

    @Override
    public boolean support(Method method) {
        return method.isAnnotationPresent(Select.class);
    }
}
