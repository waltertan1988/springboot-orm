# springboot-orm
一个基于Spring的自定义ORM框架，采用Freemarker语法解析SQL，提供接口注解、XML配置、数据表配置等3种方式进行ORM映射。
## 设计类图
![Pandao editor.md](https://github.com/waltertan1988/springboot-orm/blob/master/doc/SqlSet.jpg?raw=true "SqlSet.jpg")
## 开始使用

### 配置
* 在classpath下放入sqlset-config.xml文件，内容包括：
```html
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util.xsd">

    <!--框架数据源，alias固定为infrastructureDataSource，用于数据表配置方式来进行ORM映射-->
    <alias alias="infrastructureDataSource" name="框架数据源的beanName"/>

    <!--接口注解方式来进行ORM映射时自动扫描的package列表-->
    <bean class="org.walter.orm.processor.annotation.MethodProxyFactoryDefinitionRegistryPostProcessor">
        <property name="scanPackages">
            <set>
                <value>扫描的包1</value>
                <value>扫描的包2</value>
                <value>扫描的包N</value>
            </set>
        </property>
    </bean>
</beans>
```
* 在框架数据源中，创建数据表配置方式映射的基本表BASE_SQLSET，以MySQL为例，数据字段如下：
```sql
create table `BASE_SQLSET` (
	`id` varchar (255) COMMENT 'SqlSet的ID',
	`sqlType` varchar (255) COMMENT 'select, update, insert, delete',
	`dataSource` varchar (255) COMMENT '数据源的beanName',
	`statement` blob COMMENT 'SQL语句'
); 
```
### 使用接口注解映射

### 使用XML配置映射

### 使用数据表配置映射