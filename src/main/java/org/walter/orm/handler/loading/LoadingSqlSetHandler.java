package org.walter.orm.handler.loading;

import org.apache.commons.lang3.ArrayUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetHandler;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.executor.loading.LoadingSqlSetExecutor;
import org.walter.orm.parser.xml.loading.LoadingXmlSqlSetParser;
import org.walter.orm.throwable.SqlSetException;

@Component
public class LoadingSqlSetHandler extends AbstractSqlSetHandler {
    @Autowired
    private LoadingXmlSqlSetParser loadingXmlSqlSetParser;
    @Autowired
    private LoadingSqlSetExecutor loadingSqlSetExecutor;

    @Override
    protected void checkArgs(Object... args) {
        if(ArrayUtils.isNotEmpty(args) && args.length == 2 && (args[0] instanceof Element) && (args[1] instanceof String)){
            return;
        }
        throw new SqlSetException("Invalid args: ?", args);
    }

    @Override
    protected AbstractSqlSetParser getSqlSetParser(Object... args) {
        return loadingXmlSqlSetParser;
    }

    @Override
    protected AbstractSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet, Object... args) {
        return loadingSqlSetExecutor;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        return LoadingSqlSetHandler.class.isAssignableFrom(clz);
    }
}
