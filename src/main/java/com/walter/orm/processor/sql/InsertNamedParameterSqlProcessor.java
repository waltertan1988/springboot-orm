package com.walter.orm.processor.sql;

import com.walter.orm.annotation.Insert;
import com.walter.orm.annotation.SqlSet;
import com.walter.orm.throwable.SqlSetException;
import com.walter.orm.util.FreemarkerUtil;
import com.walter.orm.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
public class InsertNamedParameterSqlProcessor extends AbstractNamedParameterSqlProcessor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object process(Class<?> targetInterface, Object proxy, Method method, Object[] args) throws Exception {
        if(args != null && args.length != 1 && !(args[0] instanceof Map) && !ReflectionUtil.isCustomClass(args[0].getClass())){
            throw new SqlSetException("Method arg should be Map or POJO");
        }

        Insert insert = AnnotationUtils.getAnnotation(method, Insert.class);
        String sqlStatement = insert.statement();
        Object param = args[0];
        String keyField = insert.keyField();
        DataSource dataSource = getDataSource(targetInterface, insert);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        String preparedSqlStatement = FreemarkerUtil.parse(sqlStatement, param);

        return doInsert(dataSource, preparedSqlStatement, param, keyField);
    }

    private int doInsert(DataSource dataSource, String preparedSqlStatement, Object param, String keyField) throws NoSuchFieldException, IllegalAccessException {
        log.debug("sql: {}", preparedSqlStatement);
        log.debug("param: {}", param);

        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int count = namedParameterJdbcTemplate.update(preparedSqlStatement, sqlParameterSource, keyHolder);
        long keyValue = keyHolder.getKey().longValue();

        if(count > 0 && StringUtils.isNotBlank(keyField)){
            if(param instanceof Map){
                ((Map) param).put(keyField, keyValue);
            }else{
                ReflectionUtil.setBeanProperty(param, ReflectionUtil.toLowerCamel(keyField), keyValue);
            }
        }

        return count;
    }

    private DataSource getDataSource(Class<?> targetInterface, Insert insert){
        String dsName = insert.dataSourceRef();
        dsName = (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
        DataSource dataSource = applicationContext.getBean(dsName, DataSource.class);
        return dataSource;
    }

    @Override
    public Boolean support(Method method) {
        return method.isAnnotationPresent(Insert.class);
    }
}
