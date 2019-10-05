package org.walter.orm.processor.annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.walter.orm.handler.annotation.AbstractAnnotationSqlSetHandler;
import org.walter.orm.util.ApplicationContextHolder;

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
    public Object invoke(Object proxy, Method method, Object[] args) {
        AbstractAnnotationSqlSetHandler handler = applicationContext.getBeansOfType(AbstractAnnotationSqlSetHandler.class)
                .values().stream().filter(h -> h.support(h.getClass(), method)).findFirst().get();

        return handler.handle(targetInterface, method, args);
    }

    @SuppressWarnings("unused")
    public void setTargetInterface(Class<?> targetInterface) {
        this.targetInterface = targetInterface;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Inherited
    @interface TargetInterface{}
}
