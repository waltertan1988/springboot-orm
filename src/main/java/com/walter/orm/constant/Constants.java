package com.walter.orm.constant;

public class Constants {
    private Constants(){}
    public static class OrmPropertiesKey {
        private OrmPropertiesKey(){}
        public static final String ORM_SQLSET_ANNOTATION_SCAN_PACKAGES = "orm.sqlset.annotation-scan-packages";
    }
    public static class SqlSet {
        private SqlSet(){};
        public static class Statement {
            private Statement(){}
            public static final String SELECT = "select";
        }
        public static class Attribute {
            private Attribute(){}
            public static final String DATA_SOURCE_REF = "dataSourceRef";
            public static final String ID = "id";
            public static final String PARAMETER_TYPE = "parameterType";
            public static final String RESULT_TYPE = "resultType";
        }
    }
}
