package org.walter.orm.handler.annotation;

import org.walter.orm.annotation.Param;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.common.Decorator;
import org.walter.orm.core.model.AbstractSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.ReflectionUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Component
public class AnnotationUpdateSqlSetHandler extends AbstractAnnotationSqlSetHandler implements Decorator<Object[], Object[]> {
    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractSqlSetExecutor executor, Object[] args, Object... extras) {
        return super.handle(parser, executor, decorate(args, extras), extras);
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, args) && method.isAnnotationPresent(Update.class);
    }

    @Override
    public Object[] decorate(Object[] object, Object[] params) {
        Method method = (Method) object[1];
        Parameter[] parameters = method.getParameters();
        if(parameters.length != 2){
            throw new SqlSetException("Error args number");
        }

        Map<String, Object> entity = null, param = null;
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].isAnnotationPresent(Param.class)){
                param = ReflectionUtil.toMap(object[i], true);
            }else {
                entity = ReflectionUtil.toMap(object[i], true);
            }
        }

        Assert.notNull(entity, "Missing new entity");
        Assert.notNull(param, "Missing param");

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            entity.put(Update.PARAM_PREFIX + entry.getKey(), entry.getValue());
        }

        return new Object[]{entity};
    }
}
