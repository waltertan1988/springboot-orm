package com.walter.orm.processor.sql;

import com.walter.orm.annotation.Select;
import com.walter.orm.annotation.SqlSet;
import com.walter.orm.throwable.SqlSetException;
import com.walter.orm.util.FreemarkerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class SelectNamedParameterSqlProcessor extends AbstractNamedParameterSqlProcessor {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object process(Class<?> targetInterface, Object proxy, Method method, Object[] args) throws Exception {
        if(args != null && args.length > 1){
            throw new SqlSetException("Method arg should be Void, Map or POJO");
        }

        Select select = AnnotationUtils.getAnnotation(method, Select.class);
        String sqlStatement = select.statement();
        DataSource dataSource = getDataSource(targetInterface, select);
        if(dataSource == null) {
            throw new SqlSetException("Missing datasource for method ?.?()", targetInterface.getName(), method.getName());
        }

        Class<?> returnType = method.getReturnType();

        Class<?> multiReturnElementType = null;
        if(Collection.class.isAssignableFrom(returnType)){
            multiReturnElementType = select.multiReturnElementType();
        }

        Object param = null;
        if(args != null){
            param = args[0];
        }

        String preparedSqlStatement = FreemarkerUtil.parse(sqlStatement, param);

        return doSelect(dataSource, preparedSqlStatement, param, returnType, multiReturnElementType);
    }

    private Object doSelect(DataSource dataSource, String preparedSqlStatement, Object param, Class<?> returnType,
                            Class<?> multiReturnElementType) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        log.debug("sql: {}", preparedSqlStatement);
        log.debug("param: {}", param);

        SqlParameterSource sqlParameterSource = null;
        if(param instanceof Map) {
            sqlParameterSource = new MapSqlParameterSource((Map)param);
        }else if(null != param) {
            sqlParameterSource = new BeanPropertySqlParameterSource(param);
        }

        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));

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
                        Field field = multiReturnElementType.getDeclaredField(entry.getKey());
                        field.setAccessible(true);
                        field.set(element, entry.getValue());
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

    private DataSource getDataSource(Class<?> targetInterface, Select select){
        String dsName = select.dataSourceRef();
        dsName = (StringUtils.isNotBlank(dsName) ? dsName : AnnotationUtils.getAnnotation(targetInterface, SqlSet.class).dataSourceRef());
        DataSource dataSource = applicationContext.getBean(dsName, DataSource.class);
        return dataSource;
    }

    @Override
    public Boolean support(Method method) {
        return method.isAnnotationPresent(Select.class);
    }
}
