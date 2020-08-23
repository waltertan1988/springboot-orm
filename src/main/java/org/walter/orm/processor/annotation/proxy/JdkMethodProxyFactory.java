package org.walter.orm.processor.annotation.proxy;

import org.walter.orm.processor.annotation.AbstractMethodProxyFactory;

import java.lang.reflect.Proxy;

public class JdkMethodProxyFactory extends AbstractMethodProxyFactory {

    @Override
    public Object getObject() {
        Class<?>[] interfaces = new Class[]{super.targetInterface};
        return Proxy.newProxyInstance(super.targetInterface.getClassLoader(), interfaces,
                (proxy, method, args) -> super.invoke(method, args));
    }
}
