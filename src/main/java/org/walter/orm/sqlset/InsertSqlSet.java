package org.walter.orm.sqlset;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.walter.orm.core.model.AbstractSqlSet;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class InsertSqlSet extends AbstractSqlSet {
    private String keyField;

    public InsertSqlSet(String id, ConfigType configType, String dataSource, String statement, String keyField){
        super(id, configType, dataSource, statement);
        this.keyField = keyField;
    }
}
