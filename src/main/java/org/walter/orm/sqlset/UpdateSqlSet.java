package org.walter.orm.sqlset;

import org.walter.orm.core.model.AbstractSqlSet;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.sql.DataSource;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class UpdateSqlSet extends AbstractSqlSet {

    public UpdateSqlSet(String id, ConfigType configType, DataSource dataSource, String statement){
        super(id, configType, dataSource, statement);
    }
}
