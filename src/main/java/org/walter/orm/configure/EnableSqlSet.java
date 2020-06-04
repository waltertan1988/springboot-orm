package org.walter.orm.configure;

public @interface EnableSqlSet {

    /**
     * 接口注解方式来进行ORM映射时自动扫描的package列表
     * @return
     */
    String[] scanPackages() default {};

    /**
     * 框架数据源，用于数据表配置方式来进行ORM映射
     * @return
     */
    String infrastructureDataSource() default "dataSource";
}
