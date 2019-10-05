package org.walter.orm.holder;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.walter.orm.util.HolderSqlSetHandlerUtil;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbHolderSqlSetHandlerTests {
    @Test
    public void testListNameByCode(){
        Map<String, Object> condition = Maps.newHashMap("code", "00");
        Collection<String> collection = HolderSqlSetHandlerUtil.selectMany("listNameByCode", String.class, condition);
        log.debug("result: {}", collection);
    }

    @Test
    public void testOverride(){
        Map<String, Object> condition = Maps.newHashMap("code", "D0001");
        Map<String, Object> result = HolderSqlSetHandlerUtil.selectOne("override", condition);
        log.debug("result: {}", result);
    }
}
