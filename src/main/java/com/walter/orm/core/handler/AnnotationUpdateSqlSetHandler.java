package com.walter.orm.core.handler;

import com.walter.orm.annotation.Param;
import com.walter.orm.annotation.Update;
import com.walter.orm.core.executor.AbstractBaseSqlSetExecutor;
import com.walter.orm.core.parser.AbstractAnnotationSqlSetParser;
import com.walter.orm.throwable.SqlSetException;
import com.walter.orm.util.ReflectionUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Order(100)
@Component
public class AnnotationUpdateSqlSetHandler extends AnnotationSqlSetHandler {
    @Override
    public Object handle(AbstractAnnotationSqlSetParser parser, AbstractBaseSqlSetExecutor executor, Object[] args, Method method) {
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

        return super.handle(parser, executor, new Object[]{entity});
    }

    @Override
    public Boolean support(Method method) {
        return method.isAnnotationPresent(Update.class);
    }
}
