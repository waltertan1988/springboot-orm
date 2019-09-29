package org.walter.orm.handler.annotation;

import org.walter.orm.annotation.Param;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import org.walter.orm.core.model.AbstractSqlSetParser;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.ReflectionUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Component
public class AnnotationUpdateSqlSetHandler extends AbstractAnnotationSqlSetHandler {
    @Override
    public Object handle(AbstractSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Object... extras) {
        return super.handle(parser, executor, args, extras);
    }

    @Override
    protected Object[] wrapArgs(Object[] args, Object... extras) {
        Method method = (Method) extras[1];
        Parameter[] parameters = method.getParameters();
        if(parameters.length != 2){
            throw new SqlSetException("Error args number");
        }

        Map<String, Object> entity = null, param = null;
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].isAnnotationPresent(Param.class)){
                param = ReflectionUtil.toMap(args[i], true);
            }else {
                entity = ReflectionUtil.toMap(args[i], true);
            }
        }

        Assert.notNull(entity, "Missing new entity");
        Assert.notNull(param, "Missing param");

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            entity.put(Update.PARAM_PREFIX + entry.getKey(), entry.getValue());
        }

        return new Object[]{entity};
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, args) && method.isAnnotationPresent(Update.class);
    }
}
