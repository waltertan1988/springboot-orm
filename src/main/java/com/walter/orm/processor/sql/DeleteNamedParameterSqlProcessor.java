package com.walter.orm.processor.sql;

import com.walter.orm.annotation.Delete;
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
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
public class DeleteNamedParameterSqlProcessor extends AbstractNamedParameterSqlProcessor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object process(Class<?> targetInterface, Object proxy, Method method, Object[] args) {
        if(args != null && args.length != 1 && !(args[0] instanceof Map) && !ReflectionUtil.isCustomClass(args[0].getClass())){
            throw new SqlSetException("Method arg should be Map or POJO");
        }

        Delete delete = AnnotationUtils.getAnnotation(method, Delete.class);
        String sqlStatement = delete.statement();
        Object param = args[0];
        DataSource dataSource = getDataSource(targetInterface, delete);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        String preparedSqlStatement = FreemarkerUtil.parse(sqlStatement, param);

        return doDelete(dataSource, preparedSqlStatement, param);
    }

    private int doDelete(DataSource dataSource, String preparedSqlStatement, Object param) {
        log.debug("sql: {}", preparedSqlStatement);
        log.debug("param: {}", param);

        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        int count = namedParameterJdbcTemplate.update(preparedSqlStatement, sqlParameterSource);
        return count;
    }

    private DataSource getDataSource(Class<?> targetInterface, Delete delete){
        String dsName = delete.dataSourceRef();
        dsName = (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
        DataSource dataSource = applicationContext.getBean(dsName, DataSource.class);
        return dataSource;
    }

    @Override
    public Boolean support(Method method) {
        return method.isAnnotationPresent(Delete.class);
    }
}
