package com.walter.orm.core.handler;

import com.walter.orm.core.executor.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.parser.AbstractAnnotationSqlSetParser;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Order
@Component
public class AnnotationSqlSetHandler extends AbstractSqlSetHandler {

    public Object handle(AbstractAnnotationSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Method method){
        return super.handle(parser, executor, args);
    }

    public Boolean support(Method method){
        return true;
    }
}
