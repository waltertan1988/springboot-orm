package org.walter.orm.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlSet {
    @AliasFor("dataSourceRef")
    String value() default "";

    @AliasFor("value")
    String dataSourceRef() default "";
}
