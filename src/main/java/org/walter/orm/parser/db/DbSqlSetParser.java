package org.walter.orm.parser.db;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.core.model.SqlSet;

@Component
public class DbSqlSetParser extends AbstractSqlSetParser {
    @Override
    public SqlSet parse(Object... args) {
        SqlSet sqlSet = (SqlSet) args[0];
        sqlSet.setConfigType(SqlSet.ConfigType.DB);
        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return DbSqlSetParser.class.isAssignableFrom(clz);
    }
}
