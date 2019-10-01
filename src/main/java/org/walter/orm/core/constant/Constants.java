package org.walter.orm.core.constant;

public class Constants {
    private Constants(){}
    public static class OrmPropertiesKey {
        private OrmPropertiesKey(){}
        public static final String ORM_SQLSET_ANNOTATION_SCAN_PACKAGES = "orm.sqlset.annotation-scan-packages";
        public static final String ORM_SQLSET_XML_LOCATION = "/**/*-SqlSet.xml";
    }
    public static class SqlSet {
        private SqlSet(){};
        public static final String ID = "id";
        public static final String DATA_SOURCE_REF = "dataSourceRef";

        public static class Select {
            private Select(){}
            public static final String RESULT_TYPE = "resultType";
            public static final String MULTI_RETURN_ELEMENT_TYPE = "multiReturnElementType";
        }

        public static class Insert {
            private Insert(){}
            public static final String KEY_FIELD = "keyField";
        }

        public static class Delete {
            private Delete(){}
        }

        public static class Update {
            private Update(){}
        }
    }
}