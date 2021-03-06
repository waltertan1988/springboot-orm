package org.walter.orm.executor.operate;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;

public abstract class AbstractIocDataSourceSqlSetExecutor extends AbstractDataSourceSqlSetExecutor implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected DataSource getDataSource(String dataSourceRef) {
        return applicationContext.getBean(dataSourceRef, DataSource.class);
    }

    @Override
    public Boolean support(Class<?> executorType, Object... args) {
        return AbstractIocDataSourceSqlSetExecutor.class.isAssignableFrom(executorType);
    }
}
