package com.walter.orm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlSet {
    private String id;
    private Type type;
    private String dataSourceRef;
    private String parameterType;
    private String resultType;
    private Boolean multiResult;
    private String selectStatement;

    @AllArgsConstructor
    public enum Type {
        DB(0),
        XML(1);

        private int code;
    }
}
