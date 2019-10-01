package org.walter.orm.handler.annotation;

import org.walter.orm.annotation.Update;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Order
@Component
public class AnnotationDefaultSqlSetHandler extends AbstractAnnotationSqlSetHandler{
    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, method) && !method.isAnnotationPresent(Update.class);
    }
}
