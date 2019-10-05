package org.walter.orm.holder;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.handler.holder.DefaultSqlSetHolderSqlSetHandler;
import org.walter.orm.handler.holder.UpdateSqlSetHolderSqlSetHandler;
import org.walter.orm.repository.demo1.Demo1Domain;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlHolderSqlSetHandlerTests {
    @Autowired
    private DefaultSqlSetHolderSqlSetHandler defaultHandler;
    @Autowired
    private UpdateSqlSetHolderSqlSetHandler updateHandler;

    @Test
    public void testCurrentDateTime(){
        log.debug("result: {}", defaultHandler.handle("currentDateTime", Date.class, null, null));
    }

    @Test
    public void testCountAllDepartment(){
        log.debug("result: {}", defaultHandler.handle("countAllDepartment", Integer.class, null, null));
    }

    @Test
    public void testGetDepartmentName(){
        Demo1Domain condition = new Demo1Domain();
        condition.setId(1L);
        condition.setCode("00");
        log.debug("result: {}", defaultHandler.handle("getDepartmentName", String.class, null, condition));
    }

    @Test
    public void testListAllDepartmentByNameLike(){
        Map<String, Object> params = Maps.newHashMap("code", "0");
        log.debug(defaultHandler.handle("listAllDepartmentByCodeLike", Collection.class, Demo1Domain.class, params).toString());
    }

    @Test
    public void testListMapByObject(){
        Demo1Domain condition = new Demo1Domain(null, "D0001", null);
        log.debug("result: {}", defaultHandler.handle("listMapByObject", Collection.class, Demo1Domain.class, condition));
    }

    @Test
    public void testListNameByCodeIn(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("codes", Sets.newHashSet("D0001", "D0002"));
        log.debug("result: {}", defaultHandler.handle("listNameByCodeIn", Collection.class, String.class, condition));
    }

    @Test
    public void testListObjectByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("code", "%D000%");
        log.debug("result: {}", defaultHandler.handle("listObjectByMap", Collection.class, Map.class, condition));
    }

    @Test
    public void testListMapByCodeIn(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("codes", Sets.newHashSet("D0001", "D0002"));
        log.debug("result: {}", defaultHandler.handle("listMapByCodeIn", Collection.class, Map.class, condition));
    }

    @Test
    public void testGetObjectByObject(){
        Demo1Domain condition = new Demo1Domain(2L, null, null);
        log.debug("result: {}", defaultHandler.handle("getObjectByObject", Demo1Domain.class, null, condition));
    }

    @Test
    public void testGetObjectByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("code", "D0001");
        log.debug("result: {}", defaultHandler.handle("getObjectByMap", Demo1Domain.class, null, condition));
    }

    @Test
    public void testSaveObject(){
        String keyField = "id";
        Demo1Domain domain = new Demo1Domain(null, "D0004","财务部");
        long count = Long.valueOf(defaultHandler.handle("saveObject", keyField, domain).toString());
        Assert.assertTrue(1 == count);
        log.debug(domain.toString());
    }

    @Test
    public void testSaveMap(){
        String keyField = "id";
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0003");
        params.put("name", "财务部");
        long count = Long.valueOf(defaultHandler.handle("saveMap", keyField, params).toString());
        Assert.assertTrue(1 == count);
        log.debug(params.toString());
    }

    @Test
    public void testDeleteByObject(){
        Demo1Domain domain = new Demo1Domain(null, null,"财务部");
        defaultHandler.handle("deleteByObject", domain);
    }

    @Test
    public void testDeleteByMap(){
        Map<String, Object> condition = new HashMap<>();
        condition.put("id", 2L);
        long count = Long.valueOf(defaultHandler.handle("deleteByMap", condition).toString());
        log.debug("count: {}", count);
    }

    @Test
    public void testUpdateObjectByObject(){
        Demo1Domain entity = new Demo1Domain(null, "D0005", null);
        Demo1Domain condition = new Demo1Domain(null, "D0004", null);
        long result = Long.valueOf(updateHandler.handle("updateObjectByObject", entity, condition).toString());
        log.debug("count: {}", result);
    }

    @Test
    public void testUpdateObjectByObjectWithNull(){
        Demo1Domain entity = new Demo1Domain(null, null, "XXX");
        Demo1Domain condition = new Demo1Domain(null, null, "_NULL");
        long result = Long.valueOf(updateHandler.handle("updateObjectByObjectWithNull", entity, condition).toString());
        log.debug("count: {}", result);
    }

    @Test
    public void testUpdateObjectByObjectWithNotNull(){
        Demo1Domain entity = new Demo1Domain(null, null, "_NULL");
        Demo1Domain condition = new Demo1Domain(null, null, "XXX");
        long result = Long.valueOf(updateHandler.handle("updateObjectByObjectWithNull", entity, condition).toString());
        log.debug("count: {}", result);
    }
}
