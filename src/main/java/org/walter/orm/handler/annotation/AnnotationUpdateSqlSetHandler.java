package org.walter.orm.handler.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.walter.orm.annotation.Param;
import org.walter.orm.annotation.Update;
import org.walter.orm.core.model.AbstractSqlSet;
import org.walter.orm.executor.operate.AbstractDataSourceSqlSetExecutor;
import org.walter.orm.executor.operate.UpdateNamedParameterSqlSetExecutor;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
import org.walter.orm.parser.annotation.AnnotationUpdateSqlSetParser;
import org.walter.orm.throwable.SqlSetException;
import org.walter.orm.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

@Component
public class AnnotationUpdateSqlSetHandler extends AbstractAnnotationSqlSetHandler {
    @Autowired
    private AnnotationUpdateSqlSetParser parser;
    @Autowired
    private UpdateNamedParameterSqlSetExecutor executor;

    @Override
    protected AbstractAnnotationSqlSetParser getSqlSetParser(Object... args) {
        return parser;
    }

    @Override
    protected AbstractDataSourceSqlSetExecutor getSqlSetExecutor(AbstractSqlSet sqlSet, Object... args) {
        return executor;
    }

    @Override
    public Boolean support(Class<?> clz, Object... args) {
        Method method = (Method) args[0];
        return super.support(clz, args) && method.isAnnotationPresent(Update.class);
    }

    @Override
    public Object[] toExecutorArgs(Object...args) {
        Method method = (Method) args[1];
        Object[] methodArgs = (Object[]) args[INVOKE_METHOD_ARGS_IDX];
        Parameter[] parameters = method.getParameters();
        if(parameters.length != 2){
            throw new SqlSetException("Invoked method should have 2 args.");
        }

        Map<String, Object> entity = null, param = null;
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].isAnnotationPresent(Param.class)){
                param = ReflectionUtil.toMap(methodArgs[i], true);
            }else {
                entity = ReflectionUtil.toMap(methodArgs[i], true);
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
