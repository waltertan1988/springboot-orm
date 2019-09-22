package com.walter.orm.constant;

public class Constants {
    private Constants(){}

    public static class OrmPropertiesKeyConstants {
        private OrmPropertiesKeyConstants(){}
        public static final String ORM_SQLSET_ANNOTATION_SCAN_PACKAGES = "orm.sqlset.annotation-scan-packages";
    }

    public static class SqlSetAttributeConstants {
        private SqlSetAttributeConstants(){}
        public static final String ATTR_DATA_SOURCE_REF = "dataSourceRef";
        public static final String ATTR_ID = "id";
        public static final String ATTR_PARAMETER_TYPE = "parameterType";
        public static final String ATTR_RESULT_TYPE = "resultType";
    }
}
