package org.walter.orm.processor.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.walter.orm.core.common.SupportChecker;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.core.model.SqlSetHolder;
import org.walter.orm.handler.db.DbSqlSetHandler;
import org.walter.orm.util.ReflectionUtil;

import javax.sql.DataSource;

@Slf4j
@DependsOn(Constants.Infrastructure.Db.DATA_SOURCE_NAME)
@Component
public class LoadDbSqlSetPostProcessor implements BeanPostProcessor, SupportChecker {
    @Autowired
    private DbSqlSetHandler dbSqlSetHandler;
    @Autowired
    private ApplicationContext applicationContext;

    private final String INFRA_DATA_SOURCE_REF = Constants.Infrastructure.Db.DATA_SOURCE_NAME;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!support(null)){
            return bean;
        }

        DataSource ds = applicationContext.getBean(INFRA_DATA_SOURCE_REF, DataSource.class);
        new JdbcTemplate(ds).queryForList(Constants.Infrastructure.Db.QUERY_ALL_SQLSET_SQL).forEach(map -> {
            SqlSet sqlSet = ReflectionUtil.toObject(map, SqlSet.class);
            dbSqlSetHandler.handle(sqlSet);
        });
        return bean;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return SqlSetHolder.isEmpty(SqlSet.ConfigType.DB);
    }
}
