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
* 为便于测试，在业务数据源中定义数据表DEPARTMENT，并添加一些数据:
```sql
create table `department` (
	`id` bigint (20),
	`code` varchar (255),
	`name` varchar (255)
); 
insert into `department` (`id`, `code`, `name`) values('1','D0001','开发部');
insert into `department` (`id`, `code`, `name`) values('2','D0002','行政部');

insert into `base_sqlset` (`id`, `sqlType`, `dataSource`, `statement`) values('listNameByCode','select','dataSource','select name from department where code like \'%${code}%\'');
insert into `base_sqlset` (`id`, `sqlType`, `dataSource`, `statement`) values('override','select','dataSource','select * from department where code = :code');
```
### 使用接口注解映射
本框架支持注解声明式接口来进行ORM映射。

* 注解说明：

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
除了注解声明式接口方式外，本框架还支持通过编写XML映射文件来进行ORM映射。  
XML映射文件必须放在classpath能访问到的路径之下，且文件名必须以-SqlSet.xml结尾，可以有多个不同名的XML映射文件。  
映射文件的根元素\<sqlset\>中，可以用dataSourceRef指定默认数据源，其子元素\<select\>\<update\>\<insert\>\<delete\>中也可以用dataSourceRef自行覆盖。
* 定义XML映射文件：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<sqlset xmlns="http://www.waltertan.org/ORMSchema"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.waltertan.org/ORMSchema OrmSchema.xsd"
     dataSourceRef="dataSource">

    <select id="listAllDepartmentByCodeLike">
        <![CDATA[
            select * from department where code like '%${code}%'
        ]]>
    </select>

    <select id="currentDateTime">
        <![CDATA[
            select now()
        ]]>
    </select>

    <select id="getDepartmentName">
        <![CDATA[
            select name from department where id = :id and code like '%${code}%'
        ]]>
    </select>

    <select id="countAllDepartment">
        <![CDATA[
            select count(*) from department
        ]]>
    </select>

    <select id="listMapByObject">
        <![CDATA[
            select * from department
            where 1=1
            <#if name??> and name like '%${name}%'</#if>
            <#if code??> and code like :code</#if>
        ]]>
    </select>

    <select id="getMapByIdEquals1">
        <![CDATA[
            select * from department where id = 1
        ]]>
    </select>

    <select id="listMapByCodeLike00">
        <![CDATA[
            select * from department where code like '%00%'
        ]]>
    </select>

    <select id="listNameByCodeIn">
        <![CDATA[
            select name from department
            where 1=1
            and code in (:codes)
        ]]>
    </select>

    <select id="listObjectByMap">
        <![CDATA[
            select * from department
            where 1=1
            <#if name??> and name like '%${name}%'</#if>
            <#if code??> and code like :code</#if>
        ]]>
    </select>

    <select id="listMapByCodeIn">
        <![CDATA[
            select * from department
            where 1=1
            and code in (:codes)
        ]]>
    </select>

    <select id="getMapByObject">
        <![CDATA[
            select * from department
            where 1=1
            <#if id??> and id = :id</#if>
            <#if code??> and code = '${code}'</#if>
        ]]>
    </select>

    <select id="getObjectByMap">
        <![CDATA[
            select * from department
            where 1=1
            <#if id??> and id = :id</#if>
            <#if code??> and code = '${code}'</#if>
        ]]>
    </select>

    <select id="override">
        <![CDATA[
            select * from department where name = :name
        ]]>
    </select>
</sqlset>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<sqlset xmlns="http://www.waltertan.org/ORMSchema"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.waltertan.org/ORMSchema OrmSchema.xsd">

    <insert id="saveDepartment" dataSourceRef="dataSource">
        <![CDATA[
            insert into department(code, name) values (:code, :name)
        ]]>
    </insert>

    <delete id="deleteDepartmentByIdGt" dataSourceRef="dataSource">
        <![CDATA[
            delete department where id > :id
        ]]>
    </delete>

    <update id="updateObjectByObjectWithNull" dataSourceRef="dataSource">
        <![CDATA[
            update department set
                name = <#if name == '_NULL'>null<#else>:name</#if>
            where name <#if _name?? && _name == '_NULL'>is null<#else>=:_name</#if>
        ]]>
    </update>

    <insert id="saveObject" dataSourceRef="dataSource">
        <![CDATA[
            insert into department(code, name) values (:code, :name)
        ]]>
    </insert>

    <insert id="saveMap" dataSourceRef="dataSource">
        <![CDATA[
            insert into department(code, name) values (:code, :name)
        ]]>
    </insert>

    <delete id="deleteByObject" dataSourceRef="dataSource">
        <![CDATA[
            delete from department where name like '%${name}%'
        ]]>
    </delete>

    <delete id="deleteByMap" dataSourceRef="dataSource">
        <![CDATA[
            delete from department where id > :id
        ]]>
    </delete>

    <update id="updateObjectByObject" dataSourceRef="dataSource">
        <![CDATA[
            update department set code = :code where code = :_code
        ]]>
    </update>
</sqlset>
```

* 结合HolderSqlSetHandlerUtil来执行ORM操作：
```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlHolderSqlSetHandlerTests {
    @Test
    public void testCurrentDateTime(){
        Date date = HolderSqlSetHandlerUtil.selectOne("currentDateTime", Date.class);
        log.debug("result: {}", date);
    }

    @Test
    public void testCountAllDepartment(){
        int count = HolderSqlSetHandlerUtil.selectOne("countAllDepartment", Integer.class);
        log.debug("result: {}", count);
    }

    @Test
    public void testGetDepartmentName(){
        Demo1Domain condition = new Demo1Domain();
        condition.setId(1L);
        condition.setCode("00");
        String name = HolderSqlSetHandlerUtil.selectOne("getDepartmentName", String.class, condition);
        log.debug("result: {}", name);
    }

    @Test
    public void testListAllDepartmentByCodeLike(){
        Map<String, Object> params = Maps.newHashMap("code", "0");
        Collection<Demo1Domain> collection = HolderSqlSetHandlerUtil.selectMany("listAllDepartmentByCodeLike",
                Demo1Domain.class, params);
        log.debug("result: {}", collection);
    }

    @Test
    public void testListMapByObject(){
        Demo1Domain condition = new Demo1Domain(null, "D0001", null);
        Collection<Map<String, Object>> collection = HolderSqlSetHandlerUtil.selectMany("listMapByObject", condition);
        log.debug("result: {}", collection);
    }

    @Test
    public void testListObjectByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("code", "%D000%");
        Collection<Demo1Domain> collection = HolderSqlSetHandlerUtil.selectMany("listObjectByMap", Demo1Domain.class, condition);
        log.debug("result: {}", collection);
    }

    @Test
    public void testGetMapByObject(){
        Demo1Domain condition = new Demo1Domain(2L, null, null);
        Map<String, Object> result = HolderSqlSetHandlerUtil.selectOne("getMapByObject", condition);
        log.debug("result: {}", result);
    }

    @Test
    public void testGetMapByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("id", 1L);
        Map<String, Object> result = HolderSqlSetHandlerUtil.selectOne("getMapByObject", condition);
        log.debug("result: {}", result);
    }

    @Test
    public void testGetObjectByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("code", "D0001");
        Demo1Domain result = HolderSqlSetHandlerUtil.selectOne("getObjectByMap", Demo1Domain.class, condition);
        log.debug("result: {}", result);
    }

    @Test
    public void testListNameByCodeIn(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("codes", Sets.newHashSet("D0001", "D0002"));
        Collection<String> collection = HolderSqlSetHandlerUtil.selectMany("listNameByCodeIn", String.class, condition);
        log.debug("result: {}", collection);
    }

    @Test
    public void testListMapByCodeIn(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("codes", Sets.newHashSet("D0001", "D0002"));
        Collection<Map<String, Object>> collection = HolderSqlSetHandlerUtil.selectMany("listMapByCodeIn", condition);
        log.debug("result: {}", collection);
    }

    @Test
    public void testGetMapByIdEquals1(){
        Map<String, Object> result = HolderSqlSetHandlerUtil.selectOne("getMapByIdEquals1");
        log.debug("result: {}", result);
    }

    @Test
    public void testListMapByCodeLike00(){
        Collection<Map<String, Object>> collection = HolderSqlSetHandlerUtil.selectMany("listMapByCodeLike00");
        log.debug("result: {}", collection);
    }

    @Test
    public void testSaveObject(){
        String keyField = "id";
        Demo1Domain domain = new Demo1Domain(null, "D0004","财务部");
        Integer count = HolderSqlSetHandlerUtil.save("saveObject", domain, keyField);
        Assert.assertTrue(1 == count);
        log.debug(domain.toString());
    }

    @Test
    public void testSaveObjectWithoutKeyField(){
        Demo1Domain domain = new Demo1Domain(null, "D0004","人事部");
        Integer count = HolderSqlSetHandlerUtil.save("saveObject", domain);
        Assert.assertTrue(1 == count);
        log.debug(domain.toString());
    }

    @Test
    public void testSaveMap(){
        String keyField = "id";
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0003");
        params.put("name", "财务部");
        Integer count = HolderSqlSetHandlerUtil.save("saveMap", params, keyField);
        Assert.assertTrue(1 == count);
        log.debug(params.toString());
    }

    @Test
    public void testSaveMapWithoutKeyField(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0003");
        params.put("name", "人事部");
        Integer count = HolderSqlSetHandlerUtil.save("saveMap", params);
        Assert.assertTrue(1 == count);
        log.debug(params.toString());
    }

    @Test
    public void testDeleteByObject(){
        Demo1Domain domain = new Demo1Domain(null, null,"财务部");
        int count = HolderSqlSetHandlerUtil.delete("deleteByObject", domain);
        log.debug("count: {}", count);
    }

    @Test
    public void testDeleteByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("id", 2L);
        int count = HolderSqlSetHandlerUtil.delete("deleteByMap", condition);
        log.debug("count: {}", count);
    }

    @Test
    public void testUpdateObjectByObject(){
        Demo1Domain newValueObject = new Demo1Domain(null, "D0005", null);
        Demo1Domain condition = new Demo1Domain(null, "D0004", null);
        Integer count = HolderSqlSetHandlerUtil.update("updateObjectByObject", newValueObject, condition);
        log.debug("count: {}", count);
    }

    @Test
    public void testUpdateObjectByObjectWithNull(){
        Demo1Domain newValueObject = new Demo1Domain(null, null, "XXX");
        Demo1Domain condition = new Demo1Domain(null, null, "_NULL");
        Integer count = HolderSqlSetHandlerUtil.update("updateObjectByObjectWithNull", newValueObject, condition);
        log.debug("count: {}", count);
    }

    @Test
    public void testUpdateObjectByObjectWithNotNull(){
        Demo1Domain newValueObject = new Demo1Domain(null, null, "_NULL");
        Demo1Domain condition = new Demo1Domain(null, null, "XXX");
        Integer count = HolderSqlSetHandlerUtil.update("updateObjectByObjectWithNull", newValueObject, condition);
        log.debug("count: {}", count);
    }
}
```
### 使用数据表配置映射
除了使用XML文件来定义映射外，本框架还支持把映射信息定义在数据表BASE_SQLSET中。  
值得注意的是，如果BASE_SQLSET中定义的id与XML中定义的重复，则BASE_SQLSET的映射信息会把XML中同ID的映射信息给覆盖掉。  
数据表配置映射的使用方式，与XML映射方式一样，都可以用HolderSqlSetHandlerUtil工具类来执行ORM操作。

## 更多
更多使用范例，请参看源码中src/test/*下的测试用例。