package org.walter.orm.parser.xml;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.core.model.SqlSet;

@Component
public class XmlSqlSetParser extends AbstractSqlSetParser {

    private final String SQLSET_COMMENT_PATTERN = "<!--.*-->";

    @Override
    public SqlSet parse(Object... args) {
        Element sqlElement = (Element) args[0];
        final String DEFAULT_DATA_SOURCE_REF = (String) args[1];
        String id = sqlElement.attributeValue(Constants.SqlSet.ID);
        String sqlType = sqlElement.getName().toLowerCase();
        String statement = sqlElement.getText().replaceAll(SQLSET_COMMENT_PATTERN, "").trim();
        String datasource = DEFAULT_DATA_SOURCE_REF;
        String _sqlElementDatasourceRef = sqlElement.attributeValue(Constants.SqlSet.DATA_SOURCE_REF);
        if(StringUtils.isNotBlank(_sqlElementDatasourceRef)){
            datasource = _sqlElementDatasourceRef;
        }

        SqlSet sqlSet = new SqlSet();
        sqlSet.setId(id);
        sqlSet.setConfigType(SqlSet.ConfigType.XML);
        sqlSet.setSqlType(SqlSet.SqlType.valueOf(sqlType.toUpperCase()));
        sqlSet.setStatement(statement);
        sqlSet.setDataSource(datasource);
        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return XmlSqlSetParser.class.isAssignableFrom(clz);
    }
}
