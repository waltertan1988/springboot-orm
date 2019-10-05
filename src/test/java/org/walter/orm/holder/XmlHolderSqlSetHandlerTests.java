package org.walter.orm.holder;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.repository.demo1.Demo1Domain;
import org.walter.orm.util.HolderSqlSetHandlerUtil;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
