package com.walter.orm.parser;

import com.walter.orm.common.SqlSet;
import com.walter.orm.common.SqlSetHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Collection;

public abstract class AbstractOrmParser implements BeanPostProcessor {

    protected abstract Collection<SqlSet> parse();

    protected abstract SqlSet.Type supportSqlSetType();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(SqlSetHolder.isEmpty(supportSqlSetType())) {
            parse().forEach(sqlSet -> SqlSetHolder.put(sqlSet.getId(), sqlSet));
        }
        return bean;
    }

}
