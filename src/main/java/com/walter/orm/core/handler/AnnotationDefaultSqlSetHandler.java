package com.walter.orm.core.handler;

import com.walter.orm.annotation.Update;
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
    public Boolean support(Method method){
        return !method.isAnnotationPresent(Update.class);
    }
}
