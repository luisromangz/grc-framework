package com.greenriver.commons.data.fieldProperties.fields;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define properties and constraint for password fields.
 * @author luisro
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldPassword {

    int minLength() default 6;
    int maxLength() default Integer.MAX_VALUE;
}
