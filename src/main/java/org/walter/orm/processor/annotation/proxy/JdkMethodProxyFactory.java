package org.walter.orm.processor.annotation.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.walter.orm.handler.annotation.AbstractAnnotationSqlSetHandler;
import org.walter.orm.processor.annotation.AbstractMethodProxyFactory;
import org.walter.orm.util.ApplicationContextHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class JdkMethodProxyFactory extends AbstractMethodProxyFactory {

    @Override
    public Object getObject() {
        Class<?>[] interfaces = new Class[]{super.targetInterface};
        return Proxy.newProxyInstance(super.targetInterface.getClassLoader(), interfaces,
                (proxy, method, args) -> super.invoke(method, args));
    }
}
