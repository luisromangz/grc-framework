package com.greenriver.commons.data.fieldProperties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to add base information to class fields that are to be validated
 * or rendered.
 * 
 * @author luisro
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldProps {
    String label();
    String description() default  "";
    boolean propsViewExcluded() default false;
}
