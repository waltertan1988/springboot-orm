package org.walter.orm.parser.xml;

import lombok.extern.slf4j.Slf4j;
import org.walter.orm.core.model.AbstractSqlSetParser;

@Slf4j
public abstract class AbstractXmlSqlSetParser extends AbstractSqlSetParser {
    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return AbstractXmlSqlSetParser.class.isAssignableFrom(clz);
    }
}
