package org.walter.orm.processor.annotation;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.walter.orm.handler.annotation.AbstractAnnotationSqlSetHandler;
import org.walter.orm.util.ApplicationContextHolder;

import java.lang.reflect.Method;

@Slf4j
public abstract class AbstractMethodProxyFactory implements FactoryBean, ApplicationContextAware {
    @Setter
    @TargetInterface
    protected Class<?> targetInterface;

    protected ApplicationContext applicationContext;

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    public Object invoke(Method method, Object[] args) {
        AbstractAnnotationSqlSetHandler handler = applicationContext.getBeansOfType(AbstractAnnotationSqlSetHandler.class)
                .values().stream().filter(h -> h.support(h.getClass(), method)).findFirst().get();
        return handler.handle(targetInterface, method, args);
    }
}
