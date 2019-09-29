package org.walter.orm.repository.demo1;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        log.debug(results.toString());
    }

    @Test
    public void testListObjectByMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "%D000%");
        List<?> results = demo1Repository.listObjectByMap(params);
        log.debug(results.toString());
    }

    @Test
    public void testGetObjectByObject(){
        Demo1Domain result = demo1Repository.getObjectByObject(new Demo1Domain(2L, null, null));
        log.debug(result.toString());
    }

    @Test
    public void testGetObjectByMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0001");
        Demo1Domain result = demo1Repository.getObjectByMap(params);
        log.debug(result.toString());
    }

    @Test
    public void testSaveObject(){
        Demo1Domain domain = new Demo1Domain(null, "D0004","财务部");
        long count = demo1Repository.saveObject(domain);
        Assert.assertTrue(1 == count);
        log.debug(domain.toString());
    }

    @Test
    public void testSaveMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0003");
        params.put("name", "财务部");
        long count = demo1Repository.saveMap(params);
        Assert.assertTrue(1 == count);
        log.debug(params.toString());
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
