package org.walter.orm.sqlset;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.walter.orm.core.model.AbstractSqlSet;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class SelectSqlSet extends AbstractSqlSet {
    @NonNull
    private Class<?> resultType;
    private Class<?> multiReturnElementType;

    public SelectSqlSet(String id, ConfigType configType, String dataSource, String statement, Class<?> resultType, Class<?> multiReturnElementType){
        super(id, configType, dataSource, statement);
        this.resultType = resultType;
        this.multiReturnElementType = multiReturnElementType;
    }
}
