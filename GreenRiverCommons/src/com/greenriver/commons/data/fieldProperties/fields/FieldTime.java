package com.greenriver.commons.data.fieldProperties.fields;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation keeps properties and constraints for time fields.
 * @author luisro
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldTime {
    String minValue() default "";
    String maxValue() default "";
}
