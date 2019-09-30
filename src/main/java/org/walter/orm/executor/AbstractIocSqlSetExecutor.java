package org.walter.orm.executor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.walter.orm.core.model.AbstractSqlSetExecutor;

import javax.sql.DataSource;

public abstract class AbstractIocSqlSetExecutor extends AbstractSqlSetExecutor implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected DataSource getDataSource(String dataSourceRef) {
        return applicationContext.getBean(dataSourceRef, DataSource.class);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractIocSqlSetExecutor.class.isAssignableFrom(clz);
    }
}
