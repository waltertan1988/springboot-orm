package org.walter.orm.handler.annotation;

import org.walter.orm.annotation.Update;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Order
@Component
public class AnnotationDefaultSqlSetHandler extends AbstractAnnotationSqlSetHandler{
    @Override
    protected Object[] wrapArgs(Object[] args, Object... extras) {
        return args;
    }

    @Override
    public Boolean support(Class<?> clz, Method method){
        return super.support(clz, method) && !method.isAnnotationPresent(Update.class);
    }
}
