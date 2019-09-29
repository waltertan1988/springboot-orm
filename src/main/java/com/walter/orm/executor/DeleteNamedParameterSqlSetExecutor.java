package com.walter.orm.executor;

import com.walter.orm.annotation.Delete;
import com.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.model.AbstractSqlSet;
import com.walter.orm.throwable.SqlSetException;
import com.walter.orm.util.FreemarkerUtil;
import com.walter.orm.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
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
public class DeleteNamedParameterSqlSetExecutor extends AbstractBaseSqlSetExecutor {
    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        return doDelete(sqlSet.getDataSource(), sqlSet.getStatement(), args[0]);
    }

    private int doDelete(DataSource dataSource, String statement, Object param) {
        String preparedStatement = FreemarkerUtil.parse(statement, param);

        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        int count = namedParameterJdbcTemplate.update(preparedStatement, sqlParameterSource);
        return count;
    }

    @Override
    public Boolean support(Class<?> clz, Method method) {
        return super.support(clz, method) && method.isAnnotationPresent(Delete.class);
    }

    @Override
    public void preExecute(AbstractSqlSet sqlSet, Object[] args){
        super.preExecute(sqlSet, args);
        if(args != null && args.length != 1 && !(args[0] instanceof Map) && !ReflectionUtil.isCustomClass(args[0].getClass())){
            throw new SqlSetException("Method arg should be Map or POJO");
        }
    }
}
