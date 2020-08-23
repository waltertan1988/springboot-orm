package org.walter.orm.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Date currentDateTime = demoRepository.getCurrentDateTime();
        log.debug(currentDateTime.toString());
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
