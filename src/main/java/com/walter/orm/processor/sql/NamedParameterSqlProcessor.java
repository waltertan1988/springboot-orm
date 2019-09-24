package com.walter.orm.processor.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Primary
@Component
public class NamedParameterSqlProcessor extends AbstractSqlProcessor {

    @Override
    public Object process(DataSource dataSource, String preparedSqlStatement, Object param, Class<?> returnType, Class<?> multiReturnElementType) throws IllegalAccessException, InstantiationException {
        log.debug("sql: {}", preparedSqlStatement);
        log.debug("param: {}", param);

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        if(Void.class.equals(returnType)){
            return Void.class.newInstance();
        }else if(Collection.class.isAssignableFrom(returnType)) {
            List<Map<String, Object>> mapList = namedParameterJdbcTemplate.queryForList(preparedSqlStatement, sqlParameterSource);
            if(Map.class.isAssignableFrom(multiReturnElementType)){
                return mapList;
            }else{
                List<Object> resultList = new ArrayList<>(mapList.size());
                for (Map<String, Object> map : mapList) {
                    Object element = multiReturnElementType.newInstance();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        for (Field field : Object.class.getFields()) {
                            if(field.getName().equals(entry.getKey())){
                                field.setAccessible(true);
                                field.set(element, entry.getValue());
                            }
                        }
                    }
                    resultList.add(element);
                }
                return mapList;
            }
        }else if(Map.class.isAssignableFrom(returnType)){
            return namedParameterJdbcTemplate.queryForMap(preparedSqlStatement, sqlParameterSource);
        }else if(isCustomClass(returnType)){
            return namedParameterJdbcTemplate.queryForObject(preparedSqlStatement, sqlParameterSource,
                    BeanPropertyRowMapper.newInstance(returnType));
        }else {
            return namedParameterJdbcTemplate.queryForObject(preparedSqlStatement, sqlParameterSource, returnType);
        }
    }
}
