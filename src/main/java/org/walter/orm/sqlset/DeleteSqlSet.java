package org.walter.orm.sqlset;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.walter.orm.core.model.AbstractSqlSet;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class DeleteSqlSet extends AbstractSqlSet {

    public DeleteSqlSet(String id, ConfigType configType, String dataSource, String statement){
        super(id, configType, dataSource, statement);
    }
}
