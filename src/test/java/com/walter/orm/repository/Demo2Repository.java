package com.walter.orm.repository;

import com.walter.orm.annotation.SqlSet;
import com.walter.orm.annotation.SqlSetStatement;

@SqlSet
public interface Demo2Repository {
    @SqlSetStatement(value = "select 1 from department where id = #{id}", dataSourceRef = "dataSource")
    void testMethod(Long id);
}
