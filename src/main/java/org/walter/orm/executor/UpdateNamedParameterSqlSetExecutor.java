package org.walter.orm.executor;

import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.util.FreemarkerUtil;
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
public class UpdateNamedParameterSqlSetExecutor extends AbstractBaseSqlSetExecutor {
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
    public Boolean support(Class<?> clz, Method method) {
        return super.support(clz, method) && method.isAnnotationPresent(Update.class);
    }
}
