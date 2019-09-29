package org.walter.orm.parser.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.walter.orm.annotation.SqlSet;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSetParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public abstract class AbstractAnnotationSqlSetParser extends AbstractSqlSetParser {

    protected String getDataSourceName(Class<?> targetInterface, Annotation annotation){
        String dsName = null;
        try {
            dsName = String.valueOf(annotation.annotationType().getMethod(Constants.SqlSet.DATA_SOURCE_REF).invoke(annotation));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Fail to get datasource", e);
        }
        return  (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractAnnotationSqlSetParser.class.isAssignableFrom(clz);
    }
}
