package org.walter.orm.executor.operate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.SelectSqlSet;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.FreemarkerUtil;
import org.walter.orm.util.ReflectionUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SelectNamedParameterSqlSetExecutor extends AbstractIocDataSourceSqlSetExecutor {
    @Override
    public Object doExecute(AbstractSqlSet sqlSet, Object[] args) {
        SelectSqlSet selectSqlSet = (SelectSqlSet) sqlSet;
        Object param = null;
        if(args != null){
            param = args[0];
        }
        return doSelect(getDataSource(sqlSet.getDataSource()), sqlSet.getStatement(), param,
                selectSqlSet.getResultType(), selectSqlSet.getMultiReturnElementType());
    }

    private Object doSelect(DataSource dataSource, String statement, Object param, Class<?> returnType,
                            Class<?> multiReturnElementType) {
        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        String preparedSqlStatement = FreemarkerUtil.parse(statement, param);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));

        if(Void.class.equals(returnType)){
            throw new SqlSetException("Return type cannot be void");
        }else if(Collection.class.isAssignableFrom(returnType)) {
            List<Map<String, Object>> mapList = namedParameterJdbcTemplate.queryForList(preparedSqlStatement, sqlParameterSource);
            if(Map.class.isAssignableFrom(multiReturnElementType)){
                return mapList;
            }else{
                List<Object> resultList = new ArrayList<>(mapList.size());
                for (Map<String, Object> map : mapList) {
                    Object element = null;
                    try {
                        element = multiReturnElementType.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new SqlSetException(e);
                    }
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        ReflectionUtil.setBeanProperty(element, ReflectionUtil.toLowerCamel(entry.getKey()), entry.getValue());
                    }
                    resultList.add(element);
                }
                return mapList;
            }
        }else if(Map.class.isAssignableFrom(returnType)){
            return namedParameterJdbcTemplate.queryForMap(preparedSqlStatement, sqlParameterSource);
        }else if(ReflectionUtil.isCustomClass(returnType)){
            return namedParameterJdbcTemplate.queryForObject(preparedSqlStatement, sqlParameterSource,
                    BeanPropertyRowMapper.newInstance(returnType));
        }else {
            return namedParameterJdbcTemplate.queryForObject(preparedSqlStatement, sqlParameterSource, returnType);
        }
    }

    @Override
    public Boolean support(Class<?> executorType, Object...args) {
        AbstractSqlSet sqlSet = (AbstractSqlSet) args[0];
        return super.support(executorType, sqlSet) && (sqlSet instanceof SelectSqlSet);
    }

    @Override
    protected void preExecute(AbstractSqlSet sqlSet, Object[] args) {
        super.preExecute(sqlSet, args);
        if(args != null && args.length > 1){
            throw new SqlSetException("Method arg should be Void, Map or POJO");
        }
    }
}
