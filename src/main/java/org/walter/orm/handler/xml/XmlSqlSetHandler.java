package org.walter.orm.handler.xml;

import org.apache.commons.lang3.ArrayUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.executor.loading.LoadingSqlSetExecutor;
import org.walter.orm.parser.xml.XmlSqlSetParser;
import org.walter.orm.throwable.SqlSetException;

@Component
public class XmlSqlSetHandler extends AbstractSqlSetHandler {
    @Autowired
    private XmlSqlSetParser xmlSqlSetParser;
    @Autowired
    private LoadingSqlSetExecutor loadingSqlSetExecutor;

    @Override
    protected void checkArgs(Object... args) {
        if(ArrayUtils.isNotEmpty(args) && args.length == 2 && (args[0] instanceof Element) && (args[1] instanceof String)){
            return;
        }
        throw new SqlSetException("Invalid args: %s", args);
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return xmlSqlSetParser;
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(SqlSet sqlSet, Object... args) {
        return loadingSqlSetExecutor;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return XmlSqlSetHandler.class.isAssignableFrom(clz);
    }
}
