package com.walter.orm.parser.annotation;

import com.walter.orm.annotation.Select;
import com.walter.orm.core.model.AbstractSqlSet;
import com.walter.orm.sqlset.SelectSqlSet;
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
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];

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
    public Boolean support(Class<?> clz, Method method) {
        return super.support(clz, method) && method.isAnnotationPresent(Select.class);
    }
}
