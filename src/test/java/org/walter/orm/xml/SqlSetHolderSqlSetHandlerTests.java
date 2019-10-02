package org.walter.orm.xml;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.handler.holder.SqlSetHolderSqlSetHandler;

import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlSetHolderSqlSetHandlerTests {
    @Autowired
    private SqlSetHolderSqlSetHandler handler;

    @Test
    public void testListAllDepartmentByNameLike(){
        Map<String, Object> params = Maps.newHashMap("code", "0");
        log.debug(handler.handle("listAllDepartmentByCodeLike", params).toString());
    }
}
