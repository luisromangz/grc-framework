package com.greenriver.commons.data.fieldProperties.fields;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define properties for long text fields.
 * 
 * @author luisro
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldLongText {

    String customRegExp() default "";

    int minLength() default 0;

    int maxLength() default Integer.MAX_VALUE;
}
