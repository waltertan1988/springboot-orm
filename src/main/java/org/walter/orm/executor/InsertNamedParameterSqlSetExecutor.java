package org.walter.orm.executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.walter.orm.annotation.Insert;
import org.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.InsertSqlSet;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.FreemarkerUtil;
import org.walter.orm.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class InsertNamedParameterSqlSetExecutor extends AbstractBaseSqlSetExecutor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        InsertSqlSet insertSqlSet = (InsertSqlSet) sqlSet;
        return doInsert(sqlSet.getDataSource(), sqlSet.getStatement(), args[0], insertSqlSet.getKeyField());
    }

    private int doInsert(String dataSource, String statement, Object param, String keyField) {
        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        String preparedSqlStatement = FreemarkerUtil.parse(statement, param);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(getDataSource(dataSource)));
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

    @Override
    protected DataSource getDataSource(String dataSourceRef) {
        return applicationContext.getBean(dataSourceRef, DataSource.class);
    }

    @Override
    protected void preExecute(AbstractSqlSet sqlSet, Object[] args) {
        super.preExecute(sqlSet, args);
        if(args != null && args.length != 1 && !(args[0] instanceof Map) && !ReflectionUtil.isCustomClass(args[0].getClass())){
            throw new SqlSetException("Method arg should be Map or POJO");
        }
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && method.isAnnotationPresent(Insert.class);
    }
}
