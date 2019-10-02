package org.walter.orm.parser;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.sqlset.SqlSetHolder;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class XmlSqlSetParserTests {
	@Test
	public void testXmlOrmParser() {
		SqlSetHolder.getSqlSetMapList().forEach(map -> {
			map.values().forEach(sqlSet -> log.info("SqlSet: {}", sqlSet.toString()));
		});
	}
}
