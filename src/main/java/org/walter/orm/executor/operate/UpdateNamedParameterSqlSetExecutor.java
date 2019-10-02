package org.walter.orm.executor.operate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.UpdateSqlSet;
import org.walter.orm.util.FreemarkerUtil;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Component
public class UpdateNamedParameterSqlSetExecutor extends AbstractIocDataSourceSqlSetExecutor {
    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        Map<String, Object> mapParam = (Map<String, Object>) args[0];
        return doUpdate(getDataSource(sqlSet.getDataSource()), sqlSet.getStatement(), mapParam);
    }

    private int doUpdate(DataSource dataSource, String statement, Map<String, Object> param) {
        String preparedSqlStatement = FreemarkerUtil.parse(statement, param);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(param);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        int count = namedParameterJdbcTemplate.update(preparedSqlStatement, sqlParameterSource);
        return count;
    }

    @Override
    public Boolean support(Class<?> executorType, Object...args) {
        AbstractSqlSet sqlSet = (AbstractSqlSet) args[0];
        return super.support(executorType, sqlSet) && (sqlSet instanceof UpdateSqlSet);
    }
}
