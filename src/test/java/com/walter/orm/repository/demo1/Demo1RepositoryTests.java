package com.walter.orm.repository.demo1;

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
import java.util.UUID;

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
        Demo1Domain domain = new Demo1Domain(null, UUID.randomUUID().toString(),"财务部");
        long result = demo1Repository.saveObject(domain);
        log.debug(String.valueOf(result));
        Assert.assertTrue(1 == result && domain.getId() > 0);
    }

    @Test
    public void testSaveMap(){
        Map<String, Object> params = new HashMap<>();
        params.put("code", UUID.randomUUID().toString());
        params.put("name", "财务部");
        long result = demo1Repository.saveMap(params);
        log.debug("id: {}", params.get("id"));
        Assert.assertTrue(1 == result && null != params.get("id"));
    }
}
