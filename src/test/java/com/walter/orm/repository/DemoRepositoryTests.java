package com.walter.orm.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoRepositoryTests {
    @Autowired
    private DemoRepository demoRepository;

    @Test
    public void testInterfaceDynamicProxy(){
        Assert.assertNotNull(demoRepository);
        demoRepository.currentDateTime();
    }
}
