package com.walter.orm.processor;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetStatement;
import com.walter.orm.throwable.SqlSetException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import javax.sql.DataSource;
import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class SqlSetInterfaceProxyFactoryBean implements FactoryBean, InvocationHandler, ApplicationContextAware {

    @TargetInterface
    private Class<?> targetInterface;

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        Class<?>[] interfaces = new Class[]{targetInterface};
        return Proxy.newProxyInstance(targetInterface.getClassLoader(), interfaces, this);
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSetStatement sqlSetStatement = AnnotationUtils.getAnnotation(method, SqlSetStatement.class);
        String sqlStatement = sqlSetStatement.statement();
        DataSource dataSource = getDataSource(sqlSetStatement);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }
        return execute(dataSource, sqlStatement, args, method.getReturnType());
    }

    private Object execute(DataSource dataSource, String sqlStatement, Object[] args, Class<?> resultClz) throws IllegalAccessException, InstantiationException {
        log.debug("datasource: {}", dataSource.getClass().getName());
        log.debug("sqlStatement: {}", sqlStatement);
        log.debug("args: {}", args);
        log.debug("resultClass: {}", resultClz.getName());
        Object resultObject = null;
        if(Void.class.equals(resultClz)){
            resultObject = resultClz.newInstance();
        }
        return resultObject;
    }

    private DataSource getDataSource(SqlSetStatement sqlSetStatement){
        String dsName = sqlSetStatement.dataSourceRef();
        dsName = (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
        DataSource dataSource = applicationContext.getBean(dsName, DataSource.class);
        return dataSource;
    }

    @SuppressWarnings("unused")
    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface TargetInterface{}
}
