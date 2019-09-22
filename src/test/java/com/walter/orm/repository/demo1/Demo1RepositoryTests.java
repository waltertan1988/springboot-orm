package com.walter.orm.repository.demo1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo1RepositoryTests {
    @Autowired
    private Demo1Repository demo1Repository;

    @Test
    public void testMapParams(){
        Assert.assertNotNull(demo1Repository);
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0001");
        params.put("name", "IT");
        demo1Repository.testMapParam(params);
    }

    @Test
    public void testObjectParams(){
        Assert.assertNotNull(demo1Repository);
        Map<String, Object> params = new HashMap<>();
        params.put("code", "D0001");
        params.put("name", "IT");
        demo1Repository.testObjectParam(new ObjectParam(1L, "D0001", null));
    }

    @Getter
    @AllArgsConstructor
    public class ObjectParam {
        private Long id;
        private String code;
        private String name;
    }
}
