package org.walter.orm.processor.annotation;

import org.walter.orm.core.model.AbstractBaseSqlSetExecutor;
import org.walter.orm.handler.annotation.AbstractAnnotationSqlSetHandler;
import org.walter.orm.parser.annotation.AbstractAnnotationSqlSetParser;
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
                .values().stream().filter(p -> p.support(p.getClass(), method)).findFirst().get();

        AbstractBaseSqlSetExecutor executor = applicationContext.getBeansOfType(AbstractBaseSqlSetExecutor.class)
                .values().stream().filter(e -> e.support(e.getClass(), method)).findFirst().get();

        AbstractAnnotationSqlSetHandler handler = applicationContext.getBeansOfType(AbstractAnnotationSqlSetHandler.class)
                .values().stream().filter(h -> h.support(h.getClass(), method)).findFirst().get();

        return handler.handle(parser, executor, args, targetInterface, method);
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
