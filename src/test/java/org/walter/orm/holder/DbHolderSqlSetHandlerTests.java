package org.walter.orm.holder;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.handler.holder.DefaultSqlSetHolderSqlSetHandler;
import org.walter.orm.handler.holder.UpdateSqlSetHolderSqlSetHandler;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbHolderSqlSetHandlerTests {
    @Autowired
    private DefaultSqlSetHolderSqlSetHandler defaultHandler;
    @Autowired
    private UpdateSqlSetHolderSqlSetHandler updateHandler;

    @Test
    public void testListNameByCode(){
        Map<String, Object> condition = Maps.newHashMap("code", "00");
        log.debug("result: {}", defaultHandler.handle("listNameByCode", Collection.class, String.class, condition));
    }

    @Test
    public void testOverride(){
        Map<String, Object> condition = Maps.newHashMap("code", "D0001");
        log.debug("result: {}", defaultHandler.handle("override", Map.class, null, condition));
    }
}
