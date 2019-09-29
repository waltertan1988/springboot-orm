package com.walter.orm.core.annotation;

import com.walter.orm.core.executor.AbstractNamedParameterSqlSetExecutor;
import com.walter.orm.core.handler.AnnotationSqlSetHandler;
import com.walter.orm.core.parser.AbstractAnnotationSqlSetParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class MethodProxyFactory implements FactoryBean, InvocationHandler, ApplicationContextAware {

    @TargetInterface
    private Class<?> targetInterface;

    private ApplicationContext applicationContext;

    @Override
    public Object getObject() throws Exception {
        Class<?>[] interfaces = new Class[]{targetInterface};
        return Proxy.newProxyInstance(targetInterface.getClassLoader(), interfaces, this);
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AbstractAnnotationSqlSetParser parser = applicationContext.getBeansOfType(AbstractAnnotationSqlSetParser.class)
                .values().stream().filter(p -> p.support(method)).findFirst().get();

        AbstractNamedParameterSqlSetExecutor executor = applicationContext.getBeansOfType(AbstractNamedParameterSqlSetExecutor.class)
                .values().stream().filter(e -> e.support(method)).findFirst().get();

        AnnotationSqlSetHandler handler = applicationContext.getBeansOfType(AnnotationSqlSetHandler.class)
                .values().stream().filter(h -> h.support(method)).findFirst().get();

        return handler.handle(parser, executor, args, method);
    }

    @SuppressWarnings("unused")
    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface TargetInterface{}
}
