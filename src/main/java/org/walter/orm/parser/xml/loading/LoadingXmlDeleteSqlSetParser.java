package org.walter.orm.parser.xml.loading;

import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.DeleteSqlSet;

@Component
public class LoadingXmlDeleteSqlSetParser extends AbstractLoadingXmlSqlSetParser {
    @Override
    protected AbstractSqlSet newSqlSetInstanceAndSpecialParse(Element delete) {
        DeleteSqlSet sqlSet = new DeleteSqlSet();
        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Element delete = (Element) args[0];
        return super.support(clz, args) && Constants.SqlSet.Delete.class.getSimpleName().toLowerCase().equals(delete.getName());
    }
}
