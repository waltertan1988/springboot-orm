package com.walter.orm.processor;

import com.google.common.collect.Lists;
import com.google.common.reflect.ClassPath;
import com.walter.orm.annotation.SqlSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AnnotationSqlSetProcessor implements BeanDefinitionRegistryPostProcessor {

//    @Value("#{'${orm.sqlset.annotation-scan-packages}'.split(',')}")
    private List<String> scanPackageList = Arrays.asList("com.walter.orm.repository");

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.listSqlSetInterfaces().forEach(interfaceClz -> {
            // 构造SqlSet接口对应的BeanDefinition
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SqlSetInterfaceProxyFactoryBean.class);
            for (Field field : SqlSetInterfaceProxyFactoryBean.class.getDeclaredFields()) {
                if(field.isAnnotationPresent(SqlSetInterfaceProxyFactoryBean.TargetInterface.class)){
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

    private String getDataSourceValue(Class<?> clz){
        return clz.getAnnotation(SqlSet.class).dataSourceRef();
    }

    private List<Class<?>> listSqlSetInterfaces() {
        List<Class<?>> list = Lists.newArrayList();
        scanPackageList.stream().map(String::trim).filter(pkg -> StringUtils.isNotBlank(pkg)).forEach(scanPackage -> {
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
