package com.walter.orm.parser;

import com.walter.orm.sqlset.SqlSetHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlOrmParserTests {
	@Test
	public void testXmlOrmParser() {
		SqlSetHolder.getSqlSetMapList().forEach(map -> {
			map.values().forEach(sqlSet -> log.info("SqlSet: {}", sqlSet.toString()));
		});
	}
}
