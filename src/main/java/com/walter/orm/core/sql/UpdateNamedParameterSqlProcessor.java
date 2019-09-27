package com.walter.orm.core.sql;

import com.walter.orm.annotation.Param;
import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.Update;
import com.walter.orm.throwable.SqlSetException;
import com.walter.orm.util.FreemarkerUtil;
import com.walter.orm.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Slf4j
@Component
public class UpdateNamedParameterSqlProcessor extends AbstractNamedParameterSqlProcessor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object process(Class<?> targetInterface, Object proxy, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();

        if(parameters.length != 2){
            throw new SqlSetException("Error args number");
        }

        Map<String, Object> entity = null, param = null;
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].isAnnotationPresent(Param.class)){
                param = ReflectionUtil.toMap(args[i], true);
            }else {
                entity = ReflectionUtil.toMap(args[i], true);
            }
        }

        Assert.notNull(entity, "Missing new entity");
        Assert.notNull(param, "Missing param");

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            entity.put(Update.PARAM_PREFIX + entry.getKey(), entry.getValue());
        }

        Update update = AnnotationUtils.getAnnotation(method, Update.class);

        DataSource dataSource = getDataSource(targetInterface, update);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        String sqlStatement = update.statement();

        String preparedSqlStatement = FreemarkerUtil.parse(sqlStatement, entity);

        return doUpdate(dataSource, preparedSqlStatement, entity);
    }

    private int doUpdate(DataSource dataSource, String preparedSqlStatement, Map<String, Object> param) {
        log.debug("sql: {}", preparedSqlStatement);
        log.debug("param: {}", param);

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(param);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        int count = namedParameterJdbcTemplate.update(preparedSqlStatement, sqlParameterSource);
        return count;
    }

    private DataSource getDataSource(Class<?> targetInterface, Update update){
        String dsName = update.dataSourceRef();
        dsName = (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
        DataSource dataSource = applicationContext.getBean(dsName, DataSource.class);
        return dataSource;
    }

    @Override
    public Boolean support(Method method) {
        return method.isAnnotationPresent(Update.class);
    }
}
