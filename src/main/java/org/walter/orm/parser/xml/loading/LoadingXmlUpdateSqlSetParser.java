package org.walter.orm.parser.xml.loading;

import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.sqlset.UpdateSqlSet;

@Component
public class LoadingXmlUpdateSqlSetParser extends AbstractLoadingXmlSqlSetParser {
    @Override
    protected AbstractSqlSet newSqlSetInstanceAndSpecialParse(Element update) {
        UpdateSqlSet sqlSet = new UpdateSqlSet();
        return sqlSet;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Element update = (Element) args[0];
        return super.support(clz, args) && Constants.SqlSet.Update.class.getSimpleName().toLowerCase().equals(update.getName());
    }
}
