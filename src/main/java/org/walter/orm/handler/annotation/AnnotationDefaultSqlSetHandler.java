package org.walter.orm.handler.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.executor.operate.AbstractDataSourceSqlSetExecutor;
import org.walter.orm.executor.operate.AbstractIocDataSourceSqlSetExecutor;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import org.walter.orm.throwable.SqlSetException;

import java.lang.reflect.Method;
import java.util.List;

@Order
@Component
public class AnnotationDefaultSqlSetHandler extends AbstractAnnotationSqlSetHandler{
    @Autowired
    private List<AbstractAnnotationSqlSetParser> annotationSqlSetParserList;
    @Autowired
    private List<AbstractDataSourceSqlSetExecutor> iocDataSourceSqlSetExecutorList;

    @Override
    protected AbstractAnnotationSqlSetParser getSqlSetParser(Method method) {
        return annotationSqlSetParserList.stream()
                .filter(p -> p.support(AbstractAnnotationSqlSetParser.class, method))
                .findFirst().orElseThrow(() -> new SqlSetException("Cannot find parser for method: {}", method));
    }

    @Override
    protected AbstractDataSourceSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet) {
        return iocDataSourceSqlSetExecutorList.stream()
                .filter(e -> e.support(AbstractIocDataSourceSqlSetExecutor.class, sqlSet))
                .findFirst().orElseThrow(() -> new SqlSetException("Cannot find executor for sqlset: {}", sqlSet));
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && !method.isAnnotationPresent(Update.class);
    }
}
