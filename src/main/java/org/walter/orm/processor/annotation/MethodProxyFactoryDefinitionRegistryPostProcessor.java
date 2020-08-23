package org.walter.orm.processor.annotation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.walter.orm.annotation.SqlSet;
import org.walter.orm.processor.annotation.proxy.JdkMethodProxyFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

@Slf4j
public class MethodProxyFactoryDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Setter
    private Set<String> scanPackages = Sets.newHashSet();
    @Setter
    private Class<? extends AbstractMethodProxyFactory> methodProxyFactoryClz = JdkMethodProxyFactory.class;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.listSqlSetInterfaces().forEach(interfaceClz -> {
            // 构造SqlSet接口对应的BeanDefinition
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(methodProxyFactoryClz);
            for (Field field : AbstractMethodProxyFactory.class.getDeclaredFields()) {
                if(field.isAnnotationPresent(TargetInterface.class)){
                    beanDefinitionBuilder.addPropertyValue(field.getName(), interfaceClz);
                }
            }

            AbstractBeanDefinition definition = beanDefinitionBuilder.getRawBeanDefinition();

            // 注册bean
            registry.registerBeanDefinition(interfaceClz.getSimpleName(), definition);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    private List<Class<?>> listSqlSetInterfaces() {
        List<Class<?>> list = Lists.newArrayList();
        scanPackages.stream().map(String::trim).filter(pkg -> StringUtils.isNotBlank(pkg)).forEach(scanPackage -> {
            try {
                ClassPath classPath = ClassPath.from(this.getClass().getClassLoader());
                for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive(scanPackage)) {
                    Class clz = Class.forName(classInfo.getName());
                    if (clz.isInterface() && clz.isAnnotationPresent(SqlSet.class)) {
                        list.add(clz);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                log.error("Fail to load SqlSet interface.", e);
            }
        });
        return list;
    }
}
