package com.greenriver.commons.data.fieldProperties.fields;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation type defines constraints and properties of decimal numbers.
 * @author luisro
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDecimal {
    double minValue() default Double.NEGATIVE_INFINITY;
    double maxValue() default Double.POSITIVE_INFINITY;

    int minPlaces() default 0;
    int maxPlaces() default 2;    
}
