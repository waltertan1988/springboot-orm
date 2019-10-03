package org.walter.orm.parser.xml.operate;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.parser.xml.AbstractXmlSqlSetParser;
import org.walter.orm.core.model.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

import java.util.Map;

@Component
public class OperateXmlSqlSetParser extends AbstractXmlSqlSetParser {
    @Override
    public SqlSet parse(Object... args) {
        String id = (String) args[0];
        Map<String, SqlSet> sqlSetMap = SqlSetHolder.getSqlSetMapList().stream()
                .filter(ssm -> ssm.containsKey(id))
                .findFirst().orElseThrow(() -> new SqlSetException("No SqlSet found for id[{}]", id));
        return sqlSetMap.get(id);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return OperateXmlSqlSetParser.class.isAssignableFrom(clz);
    }
}
