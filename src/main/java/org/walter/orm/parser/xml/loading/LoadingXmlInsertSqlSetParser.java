package org.walter.orm.parser.xml.loading;

import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.InsertSqlSet;

@Component
public class LoadingXmlInsertSqlSetParser extends AbstractLoadingXmlSqlSetParser {
    @Override
    protected AbstractSqlSet newSqlSetInstanceAndSpecialParse(Element insert) {
        InsertSqlSet sqlSet = new InsertSqlSet();
        String keyField = insert.attributeValue(Constants.SqlSet.Insert.KEY_FIELD);
        sqlSet.setKeyField(keyField);
        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Element insert = (Element) args[0];
        return super.support(clz, args) && Constants.SqlSet.Insert.class.getSimpleName().toLowerCase().equals(insert.getName());
    }
}
