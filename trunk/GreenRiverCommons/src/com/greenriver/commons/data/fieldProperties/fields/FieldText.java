package com.greenriver.commons.data.fieldProperties.fields;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to define properties and constrains applicable
 * to text data fields.
 *
 * @author luisro
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldText {
    boolean required() default true;
    String invalidMessage() default "";
    boolean editable() default true;
    String unit() default "";

    String customRegExp() default "";
    
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;

    boolean autoComplete=false;
}
