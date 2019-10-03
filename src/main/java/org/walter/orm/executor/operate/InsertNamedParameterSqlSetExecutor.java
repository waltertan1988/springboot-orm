package org.walter.orm.executor.operate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.FreemarkerUtil;
import org.walter.orm.util.ReflectionUtil;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Component
public class InsertNamedParameterSqlSetExecutor extends AbstractIocDataSourceSqlSetExecutor {
    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        if(ArrayUtils.isEmpty(args) || args.length < 2){
            throw new SqlSetException("Invalid args: %s", args);
        }

        String keyField = (String) args[0];
        return doInsert(getDataSource(sqlSet.getDataSource()), sqlSet.getStatement(), args[1], keyField);
    }

    private int doInsert(DataSource dataSource, String statement, Object param, String keyField) {
        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        String preparedSqlStatement = FreemarkerUtil.parse(statement, param);
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

    @Override
    protected void preExecute(AbstractSqlSet sqlSet, Object[] args) {
        super.preExecute(sqlSet, args);
        if(args != null && args.length > 2 && !(args[0] instanceof Map) && !ReflectionUtil.isCustomClass(args[0].getClass())){
            throw new SqlSetException("Method arg should be Map or POJO");
        }
    }

    @Override
    public Boolean support(Class<?> executorType, Object...args) {
        AbstractSqlSet sqlSet = (AbstractSqlSet) args[0];
        return super.support(executorType, sqlSet) && (AbstractSqlSet.SqlType.INSERT.equals(sqlSet.getSqlType()));
    }
}
