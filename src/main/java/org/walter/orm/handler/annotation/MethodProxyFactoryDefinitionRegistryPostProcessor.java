package org.walter.orm.handler.annotation;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import org.walter.orm.annotation.SqlSet;
import org.walter.orm.core.constant.Constants;
import org.walter.orm.throwable.SqlSetException;
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
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Slf4j
@Component
public class MethodProxyFactoryDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private Set<String> scanPackages = Sets.newHashSet();

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.loadScanPackages();

        this.listSqlSetInterfaces().forEach(interfaceClz -> {
            // 构造SqlSet接口对应的BeanDefinition
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MethodProxyFactory.class);
            for (Field field : MethodProxyFactory.class.getDeclaredFields()) {
                if(field.isAnnotationPresent(MethodProxyFactory.TargetInterface.class)){
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

    private void loadScanPackages(){
        try {
            Properties properties = new Properties();
            properties.load(this.getClass().getResourceAsStream("/orm.properties"));
            String pkgs = properties.getProperty(Constants.OrmPropertiesKey.ORM_SQLSET_ANNOTATION_SCAN_PACKAGES);
            for (String pkg : StringUtils.deleteWhitespace(pkgs).split(",")) {
                scanPackages.add(pkg);
            }
        } catch (IOException e) {
            throw new SqlSetException(e);
        }
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
