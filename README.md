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
* 相关注解说明：

@SqlSet - 声明一个接口为SqlSet类型的接口

属性  | 描述
------------- | -------------
value  | 默认使用的数据源beanName（可选）
dataSourceRef  | 同value

@Select - 在SqlSet接口中声明一个方法为查询方法

属性  | 描述
------------- | -------------
value  | SQL语句
statement  | 同value
multiReturnElementType  | 配合返回值为集合的方法，定义集合的元素类型（可选，默认为Map）
dataSourceRef  | 使用的数据源beanName（可选），可以覆盖@SqlSet中定义的默认数据源

@Insert - 在SqlSet接口中声明一个方法为插入方法

属性  | 描述
------------- | -------------
value  | SQL语句
statement  | 同value
keyField  | 定义主键自增长时，返回的主键值写入哪个字段中（可选）
dataSourceRef  | 使用的数据源beanName（可选），可以覆盖@SqlSet中定义的默认数据源

@Delete - 在SqlSet接口中声明一个方法为删除方法

属性  | 描述
------------- | -------------
value  | SQL语句
statement  | 同value
dataSourceRef  | 使用的数据源beanName（可选），可以覆盖@SqlSet中定义的默认数据源

@Update - 在SqlSet接口中声明一个方法为更新方法

属性  | 描述
------------- | -------------
value  | SQL语句
statement  | 同value
dataSourceRef  | 使用的数据源beanName（可选），可以覆盖@SqlSet中定义的默认数据源

@Param - 配合@Update注解使用，在SqlSet接口的更新方法中，声明一个参数作为更新条件

* 使用范例：

定义SqlSet接口：
```java
@SqlSet("dataSource")
public interface DemoRepository {
    @Select("select now()")
    Date getCurrentDateTime();

    @Select("select name from department where id = :id")
    String getDepartmentName(Map<String, Object> param);

    @Select("select count(0) from department")
    long countAll();
}

@SqlSet
public interface Demo1Repository {
    @Select(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource")
    List<?> listMapByObject(Object param);

    @Select(statement = "select name from department " +
            "where 1=1 " +
            "and code in (:codes)",
            dataSourceRef = "dataSource", multiReturnElementType = String.class)
    List<String> listNameByCodeIn(Map<String, Object> params);

    @Select(value = "select * from department where 1=1" +
            "<#if name??> and name like '%${name}%'</#if>" +
            "<#if code??> and code like :code</#if>",
            dataSourceRef = "dataSource", multiReturnElementType = Demo1Domain.class)
    List<Demo1Domain> listObjectByMap(Map<String, Object> params);

    @Select(statement = "select * from department" +
            "where 1=1 " +
            "and code in (:codes)", dataSourceRef = "dataSource")
    List<?> listMapByCodeIn(Map<String, Object> params);

    @Select(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Map<String, Object> getMapByObject(Demo1Domain param);

    @Select(value = "select * from department where 1=1" +
            "<#if id??> and id = :id</#if>" +
            "<#if code??> and code = '${code}'</#if>",
            dataSourceRef = "dataSource")
    Demo1Domain getObjectByMap(Map<String, Object> params);

    @Insert(value = "insert into department(code, name) values (:code, :name)", keyField = "id", dataSourceRef = "dataSource")
    int saveObject(Demo1Domain demo1Domain);

    @Insert(value = "insert into department(code, name) values (:code, :name)", keyField = "id", dataSourceRef = "dataSource")
    int saveMap(Map<String, Object> map);

    @Delete(value = "delete from department where name like '%${name}%'", dataSourceRef = "dataSource")
    void deleteByObject(Demo1Domain demo1Domain);

    @Delete(value = "delete from department where id > :id", dataSourceRef = "dataSource")
    int deleteByMap(Map<String, Object> map);

    @Update(value = "update department set code = :code where code = :_code", dataSourceRef = "dataSource")
    int updateObjectByObject(Demo1Domain newDomain, @Param Demo1Domain param);

    @Update(value = "update department set " +
            "name = <#if name == '_NULL'>null<#else>:name</#if> " +
            "where name <#if _name?? && _name == '_NULL'>is null<#else>=:_name</#if>"
            , dataSourceRef = "dataSource")
    int updateObjectByObjectWithNull(Demo1Domain newDomain, @Param Demo1Domain param);
}
```

使用SqlSet接口：
```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRepositoryTests {
    @Autowired
    private DemoRepository demoRepository;

    @Test
    public void testDynamicProxy(){
        Assert.assertNotNull(demoRepository);
    }

    @Test
    public void testGetCurrentDateTime(){
        log.debug(demoRepository.getCurrentDateTime().toString());
    }

    @Test
    public void testGetDepartmentName(){
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1L);
        log.debug(demoRepository.getDepartmentName(param));
    }

    @Test
    public void testCountAll(){
        log.debug(String.valueOf(demoRepository.countAll()));
    }
}

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo1RepositoryTests {
    @Autowired
    private Demo1Repository demo1Repository;

    @Test
    public void testDynamicProxy(){
        Assert.assertNotNull(demo1Repository);
    }

    @Test
    public void testListMapByObject(){
        List<?> results = demo1Repository.listMapByObject(new Demo1Domain(null, "D0001", null));
        log.debug("result: {}", results.toString());
    }

    @Test
    public void testListObjectByMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "%D000%");
        List<?> results = demo1Repository.listObjectByMap(params);
        log.debug("result: {}", results.toString());
    }

    @Test
    public void testGetObjectByMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0001");
        Demo1Domain result = demo1Repository.getObjectByMap(params);
        log.debug("result: {}", result.toString());
    }

    @Test
    public void testGetMayByObject(){
        Map<String, Object> result = demo1Repository.getMapByObject(new Demo1Domain(2L, null, null));
        log.debug("result: {}", result.toString());
    }

    @Test
    public void testListMapByCodeIn(){
        Map<String, Object> params = new HashMap<>();
        params.put("codes", Sets.newHashSet("D0001", "D0002"));
        List<?> results = demo1Repository.listMapByCodeIn(params);
        log.debug("result: {}", results.toString());
    }

    @Test
    public void testListNameByCodeIn(){
        Map<String, Object> params = new HashMap<>();
        params.put("codes", Sets.newHashSet("D0001", "D0002"));
        List<String> results = demo1Repository.listNameByCodeIn(params);
        log.debug("result: {}", results.toString());
    }

    @Test
    public void testSaveObject(){
        Demo1Domain domain = new Demo1Domain(null, "D0004","财务部");
        long count = demo1Repository.saveObject(domain);
        Assert.assertTrue(1 == count);
        log.debug("result: {}", domain.toString());
    }

    @Test
    public void testSaveMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0003");
        params.put("name", "财务部");
        long count = demo1Repository.saveMap(params);
        Assert.assertTrue(1 == count);
        log.debug("result: {}", params.toString());
    }

    @Test
    public void testDeleteByObject(){
        Demo1Domain domain = new Demo1Domain(null, null,"财务部");
        demo1Repository.deleteByObject(domain);
    }

    @Test
    public void testDeleteByMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("id", 2L);
        long result = demo1Repository.deleteByMap(params);
        log.debug("count: {}", result);
    }

    @Test
    public void testUpdateObjectByObject(){
        Demo1Domain entity = new Demo1Domain(null, "D0005", null);
        Demo1Domain param = new Demo1Domain(null, "D0004", null);
        long result = demo1Repository.updateObjectByObject(entity, param);
        log.debug("count: {}", result);
    }

    @Test
    public void testUpdateObjectByObjectWithNull(){
        Demo1Domain entity = new Demo1Domain(null, null, "XXX");
        Demo1Domain param = new Demo1Domain(null, null, "_NULL");
        long result = demo1Repository.updateObjectByObjectWithNull(entity, param);
        log.debug("count: {}", result);
    }

    @Test
    public void testUpdateObjectByObjectWithNotNull(){
        Demo1Domain entity = new Demo1Domain(null, null, "_NULL");
        Demo1Domain param = new Demo1Domain(null, null, "XXX");
        long result = demo1Repository.updateObjectByObjectWithNull(entity, param);
        log.debug("count: {}", result);
    }
}
```
### 使用XML配置映射

### 使用数据表配置映射