package com.walter.orm.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.Map;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlSetSelect {
    @AliasFor("statement")
    String value() default "";

    @AliasFor("value")
    String statement() default "";

    Class<?> multiReturnElementType() default Map.class;

    String dataSourceRef() default "";
}
