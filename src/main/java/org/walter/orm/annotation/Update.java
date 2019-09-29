package org.walter.orm.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Update {

    String PARAM_PREFIX = "_";

    @AliasFor("statement")
    String value() default "";

    @AliasFor("value")
    String statement() default "";

    String dataSourceRef() default "";
}
