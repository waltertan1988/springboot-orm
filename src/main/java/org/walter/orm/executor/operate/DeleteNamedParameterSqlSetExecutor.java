package org.walter.orm.executor.operate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.DeleteSqlSet;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.FreemarkerUtil;
import org.walter.orm.util.ReflectionUtil;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
@Component
public class DeleteNamedParameterSqlSetExecutor extends AbstractIocDataSourceSqlSetExecutor {
    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        return doDelete(getDataSource(sqlSet.getDataSource()), sqlSet.getStatement(), args[0]);
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
    public Boolean support(Class<?> executorType, Object...args) {
        AbstractSqlSet sqlSet = (AbstractSqlSet) args[0];
        return super.support(executorType, sqlSet) && (sqlSet instanceof DeleteSqlSet);
    }

    @Override
    public void preExecute(AbstractSqlSet sqlSet, Object[] args){
        super.preExecute(sqlSet, args);
        if(args != null && args.length != 1 && !(args[0] instanceof Map) && !ReflectionUtil.isCustomClass(args[0].getClass())){
            throw new SqlSetException("Method arg should be Map or POJO");
        }
    }
}
