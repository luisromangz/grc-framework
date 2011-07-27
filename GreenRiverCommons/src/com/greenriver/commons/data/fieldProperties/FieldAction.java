package com.greenriver.commons.data.fieldProperties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author luis
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldAction {
    public String targetField() default "";
    public String triggerField();
    public String triggerValue();
    public boolean equals() default true;
    public String newValue() default  "\0";
    public boolean deactivate() default false;
}
