package org.walter.orm.parser;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.core.model.SqlSet;
import org.walter.orm.core.model.SqlSetHolder;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbSqlSetParserTests {
	@Test
	public void testDbOrmParser() {
		SqlSetHolder.getSqlSetMapList().get(SqlSet.ConfigType.DB.ordinal()).values()
				.forEach(sqlSet -> log.info("SqlSet: {}", sqlSet.toString()));
	}
}
