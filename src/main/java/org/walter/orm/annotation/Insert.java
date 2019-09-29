package org.walter.orm.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Insert {
    @AliasFor("statement")
    String value() default "";

    @AliasFor("value")
    String statement() default "";

    String keyField() default "";

    String dataSourceRef() default "";
}
