package org.walter.orm.processor.annotation.proxy;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;

@Slf4j
public class JavassistTests {

    interface TestRepository{
        String method1(Date date);
        void method2();
        int method3(double num);
    }

    @Test
    public void testDynamicProxy() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        // 创建一个空的代理类并设置其代理的接口
        CtClass ctClassProxy = pool.makeClass(getProxyClassName());
        CtClass ctClassTargetInterface = pool.get(TestRepository.class.getName());
        ctClassProxy.setInterfaces(new CtClass[]{ctClassTargetInterface});

        // 对代理类实现其接口方法（空方法）
        for (CtMethod iMethod : ctClassTargetInterface.getDeclaredMethods()) {
            CtMethod method = new CtMethod(iMethod.getReturnType(), iMethod.getName(), iMethod.getParameterTypes(), ctClassProxy);
            method.setBody(null);
            ctClassProxy.addMethod(method);
        }

        // 生成.class文件
        // ctClassProxy.writeFile("D:/");

        System.out.println(ctClassProxy.toClass().newInstance());
    }

    private String getProxyClassName(){
        return new StringBuilder(TestRepository.class.getName()).append("$Impl").toString();
    }
}
