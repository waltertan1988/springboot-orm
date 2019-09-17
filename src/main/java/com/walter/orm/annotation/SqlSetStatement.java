package com.walter.orm.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlSetStatement {
    String dataSourceRef() default "";
    String statement();
}
