package org.walter.orm.parser.xml.operate;

import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.parser.xml.AbstractXmlSqlSetParser;
import org.walter.orm.sqlset.SqlSetHolder;
import org.walter.orm.throwable.SqlSetException;

import java.util.Map;

@Component
public class OperateXmlSqlSetParser extends AbstractXmlSqlSetParser {
    @Override
    public AbstractSqlSet parse(Object... args) {
        String id = (String) args[0];
        Map<String, AbstractSqlSet> sqlSetMap = SqlSetHolder.getSqlSetMapList().stream()
                .filter(ssm -> ssm.containsKey(id))
                .findFirst().orElseThrow(() -> new SqlSetException("No SqlSet found for id[{}]", id));
        return sqlSetMap.get(id);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return OperateXmlSqlSetParser.class.isAssignableFrom(clz);
    }
}
