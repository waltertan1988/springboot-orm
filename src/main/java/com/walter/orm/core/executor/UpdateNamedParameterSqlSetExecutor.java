package com.walter.orm.core.executor;

import com.walter.orm.annotation.Update;
import com.walter.orm.core.sqlset.AbstractSqlSet;
import com.walter.orm.util.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Component
public class UpdateNamedParameterSqlSetExecutor extends AbstractNamedParameterSqlSetExecutor {
    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        Map<String, Object> mapParam = (Map<String, Object>) args[0];
        return doUpdate(sqlSet.getDataSource(), sqlSet.getStatement(), mapParam);
    }

    private int doUpdate(DataSource dataSource, String statement, Map<String, Object> param) {
        String preparedSqlStatement = FreemarkerUtil.parse(statement, param);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(param);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        int count = namedParameterJdbcTemplate.update(preparedSqlStatement, sqlParameterSource);
        return count;
    }

    @Override
    public Boolean support(Method method) {
        return method.isAnnotationPresent(Update.class);
    }
}
