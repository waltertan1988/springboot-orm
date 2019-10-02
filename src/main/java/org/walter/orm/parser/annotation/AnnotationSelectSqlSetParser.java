package org.walter.orm.parser.annotation;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Select;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.SelectSqlSet;

import java.lang.reflect.Method;
import java.util.Collection;

@Component
public class AnnotationSelectSqlSetParser extends AbstractAnnotationSqlSetParser {
    @Override
    public AbstractSqlSet parse(Object... extras) {
        Class<?> targetInterface = (Class<?>) extras[0];
        Method method = (Method) extras[1];

        Select select = AnnotationUtils.getAnnotation(method, Select.class);
        String sqlStatement = select.statement();
        String dataSource = getDataSourceName(targetInterface, select);
        Class<?> returnType = method.getReturnType();
        Class<?> multiReturnElementType = null;
        if(Collection.class.isAssignableFrom(returnType)){
            multiReturnElementType = select.multiReturnElementType();
        }

        return new SelectSqlSet(null, AbstractSqlSet.ConfigType.ANNOTATION, dataSource, sqlStatement, returnType, multiReturnElementType);
    }

    @Override
    protected Boolean support(Method method) {
        return method.isAnnotationPresent(Select.class);
    }
}
