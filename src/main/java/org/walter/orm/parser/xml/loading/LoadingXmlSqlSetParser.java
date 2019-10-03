package org.walter.orm.parser.xml.loading;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.parser.xml.AbstractXmlSqlSetParser;

@Component
public class LoadingXmlSqlSetParser extends AbstractXmlSqlSetParser {

    private final String SQLSET_COMMENT_PATTERN = "<!--.*-->";

    @Override
    public AbstractSqlSet parse(Object... args) {
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

        AbstractSqlSet sqlSet = new AbstractSqlSet();
        sqlSet.setId(id);
        sqlSet.setConfigType(AbstractSqlSet.ConfigType.XML);
        sqlSet.setSqlType(AbstractSqlSet.SqlType.valueOf(sqlType));
        sqlSet.setStatement(statement);
        sqlSet.setDataSource(datasource);
        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return LoadingXmlSqlSetParser.class.isAssignableFrom(clz);
    }
}
