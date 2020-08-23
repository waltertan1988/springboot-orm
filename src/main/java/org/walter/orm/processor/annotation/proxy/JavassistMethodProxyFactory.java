package org.walter.orm.processor.annotation.proxy;

import javassist.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.walter.orm.handler.annotation.AbstractAnnotationSqlSetHandler;
import org.walter.orm.processor.annotation.AbstractMethodProxyFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JavassistMethodProxyFactory extends AbstractMethodProxyFactory {

    private final String PROXY_APPLICATION_CONTEXT_FIELD_NAME = "applicationContext";

    @Override
    public Object getObject() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        // 创建一个空的代理类，并设置其代理的接口
        CtClass ctClassProxy = pool.makeClass(getProxyClassName());
        CtClass ctClassTargetInterface = pool.get(targetInterface.getName());
        ctClassProxy.setInterfaces(new CtClass[]{ctClassTargetInterface, pool.get(ApplicationContextAware.class.getName())});

        // 代理类 设置applicationContext字段及setter
        CtField appCtx = new CtField(pool.get(ApplicationContext.class.getName()), PROXY_APPLICATION_CONTEXT_FIELD_NAME, ctClassProxy);
        appCtx.setModifiers(Modifier.PRIVATE);
        ctClassProxy.addField(appCtx);
        ctClassProxy.addMethod(CtNewMethod.setter(ApplicationContextAware.class.getDeclaredMethods()[0].getName(), appCtx));

        // 对代理类实现其接口方法
        for (int i = 0; i < ctClassTargetInterface.getDeclaredMethods().length; i++) {
            CtMethod iCtMethod = ctClassTargetInterface.getDeclaredMethods()[i];
            CtMethod ctMethod = new CtMethod(iCtMethod.getReturnType(), iCtMethod.getName(), iCtMethod.getParameterTypes(), ctClassProxy);
            ctMethod.setBody(getProxyMethodBody(i, targetInterface, CtClass.voidType.equals(iCtMethod.getReturnType())));
            ctClassProxy.addMethod(ctMethod);
        }

        // 创建并返回代理对象
        ApplicationContextAware proxyObject = (ApplicationContextAware) ctClassProxy.toClass().newInstance();
        proxyObject.setApplicationContext(super.applicationContext);
        return proxyObject;
    }

    private String getProxyClassName(){
        return new StringBuilder(targetInterface.getName()).append("$Impl").toString();
    }

    /**
     * 设置代理方法体
     * @param methodIndex
     * @param targetInterface
     * @param isVoidReturn
     * @return
     */
    private String getProxyMethodBody(int methodIndex, Class<?> targetInterface, boolean isVoidReturn){

        String result = String.format("{" +
                        "Object result = null;" +
                        "%s method = %s.class.getDeclaredMethods()[%d];" +
                        "%s handlers = new %s(applicationContext.getBeansOfType(%s.class).values());" +
                        "for (int i=0; i<handlers.size(); i++) {" +
                            "%s h = (%s) handlers.get(i);" +
                            "Boolean support = h.support(h.getClass(), new Object[]{method});" +
                            "if(support.booleanValue()){" +
                                "result = h.handle(new Object[]{%s.class, method, $args});" +
                                "break;" +
                            "}" +
                        "}" +
                        "return %s;" +
                    "}",
                Method.class.getName(), targetInterface.getName(), methodIndex,
                List.class.getName(), ArrayList.class.getName(), AbstractAnnotationSqlSetHandler.class.getName(),
                AbstractAnnotationSqlSetHandler.class.getName(), AbstractAnnotationSqlSetHandler.class.getName(),
                targetInterface.getName(),
                isVoidReturn ? StringUtils.EMPTY : "($r)result"
                );

        log.debug(result);

        return result;
    }
}
