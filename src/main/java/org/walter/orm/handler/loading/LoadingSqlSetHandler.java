package org.walter.orm.handler.loading;

import org.dom4j.Element;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;

@Component
public class LoadingSqlSetHandler extends AbstractSqlSetHandler {
    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractSqlSetExecutor executor, Object[] args, Object... extras) {
        Element element = (Element) extras[0];
        String DEFAULT_DATA_SOURCE_REF = (String) extras[1];
        AbstractSqlSet sqlSet = parser.parse(element, DEFAULT_DATA_SOURCE_REF);
        return executor.execute(sqlSet, args);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return LoadingSqlSetHandler.class.isAssignableFrom(clz);
    }
}
