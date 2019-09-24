package com.walter.orm.processor.proxy;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetSelect;
import com.walter.orm.throwable.SqlSetException;
import com.walter.orm.util.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        if(args != null && args.length > 1){
            throw new SqlSetException("Method args should be Map or POJO");
        }

        SqlSetSelect sqlSetSelect = AnnotationUtils.getAnnotation(method, SqlSetSelect.class);
        String sqlStatement = sqlSetSelect.statement();
        DataSource dataSource = getDataSource(sqlSetSelect);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        Class<?> returnType = method.getReturnType();

        Class<?> multiReturnElementType = null;
        if(Collection.class.isAssignableFrom(returnType)){
            multiReturnElementType = sqlSetSelect.multiReturnElementType();
        }

        Object param = null;
        if(args != null){
            param = args[0];
        }
        return execute(dataSource, sqlStatement, param, returnType, multiReturnElementType);
    }

    private Object execute(DataSource dataSource, String sqlStatement, Object param, Class<?> returnType, Class<?> multiReturnElementType) throws IllegalAccessException, InstantiationException {
        String preparedSqlStatement = FreemarkerUtil.parse(sqlStatement, param);
        log.debug("sql: {}", preparedSqlStatement);
        log.debug("param: {}", param);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        if(Void.class.equals(returnType)){
            return Void.class.newInstance();
        }else if(Collection.class.isAssignableFrom(returnType)) {
            List<Map<String, Object>> mapList = namedParameterJdbcTemplate.queryForList(preparedSqlStatement, sqlParameterSource);
            if(Map.class.isAssignableFrom(multiReturnElementType)){
                return mapList;
            }else{
                List<Object> resultList = new ArrayList<>(mapList.size());
                for (Map<String, Object> map : mapList) {
                    Object element = multiReturnElementType.newInstance();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        for (Field field : Object.class.getFields()) {
                            if(field.getName().equals(entry.getKey())){
                                field.setAccessible(true);
                                field.set(element, entry.getValue());
                            }
                        }
                    }
                    resultList.add(element);
                }
                return mapList;
            }
        }else if(Map.class.isAssignableFrom(returnType)){
            return namedParameterJdbcTemplate.queryForMap(preparedSqlStatement, sqlParameterSource);
        }else if(isCustomClass(returnType)){
            return namedParameterJdbcTemplate.queryForObject(preparedSqlStatement, sqlParameterSource,
                    BeanPropertyRowMapper.newInstance(returnType));
        }else {
            return namedParameterJdbcTemplate.queryForObject(preparedSqlStatement, sqlParameterSource, returnType);
        }
    }

    private boolean isCustomClass(Class<?> clz){
        Assert.notNull(clz, "input object cannot be null");
        return clz.getClassLoader() != null;
    }

    private DataSource getDataSource(SqlSetSelect sqlSetSelect){
        String dsName = sqlSetSelect.dataSourceRef();
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
