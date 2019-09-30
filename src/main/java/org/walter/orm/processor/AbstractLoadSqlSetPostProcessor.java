package org.walter.orm.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.walter.orm.core.common.SupportChecker;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

import java.util.Set;

public abstract class AbstractLoadSqlSetPostProcessor implements BeanPostProcessor, SupportChecker {

    protected abstract Set<AbstractSqlSet> getSqlSets();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof AbstractLoadSqlSetPostProcessor && support(null)){
            getSqlSets().forEach(sqlSet -> {
                boolean duplicated = !SqlSetHolder.put(sqlSet);
                if(duplicated) {
                    throw new SqlSetException("Duplicated SqlSet: {}", sqlSet);
                }
            });
        }
        return bean;
    }
}
