package com.greenriver.commons.data.fieldProperties.fields;

/**
 * This annotation is used to define properties and constraints of number fields.
 *
 *
 * @author luisro
 */
public @interface FieldInteger {
    int minValue() default 0;
    int maxValue() default Integer.MAX_VALUE;
}
