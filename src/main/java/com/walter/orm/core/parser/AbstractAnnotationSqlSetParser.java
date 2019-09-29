package com.walter.orm.core.parser;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.constant.Constants;
import com.walter.orm.core.sqlset.AbstractSqlSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public abstract class AbstractAnnotationSqlSetParser {

    public abstract AbstractSqlSet parse(Class<?> targetInterface, Method method);

    public abstract boolean support(Method method);

    protected String getDataSourceName(Class<?> targetInterface, Annotation annotation){
        String dsName = null;
        try {
            dsName = String.valueOf(annotation.annotationType().getMethod(Constants.SqlSet.DATA_SOURCE_REF).invoke(annotation));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.error("Fail to get datasource", e);
        }
        return  (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
    }
}
