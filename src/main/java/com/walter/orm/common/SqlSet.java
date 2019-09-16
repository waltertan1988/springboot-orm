package com.walter.orm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SqlSet {
    private String id;
    private Type type;
    private String dataSourceRef;
    private String parameterType;
    private String resultType;

    @AllArgsConstructor
    public enum Type {
        DB(0),
        ANNOTATION(1),
        XML(2);

        private int code;
    }
}
