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
    /**
     * The name of the field that will be modified.
     * @return 
     */
    public String targetField() default "";
    /**
     * The name of the field that triggers the change.
     * @return 
     */
    public String triggerField();
    /**
     * The value that triggers the change.
     * @return 
     */
    public String triggerValue();
    /**
     * If true, the action will be triggered when the field's value is equal to
     * the specified value. Else, it will be triggered when the value is not equal.
     * @return 
     */
    public boolean equals() default true;
    /**
     * The value to set in the target field when the action is triggered.
     * @return 
     */
    public String newValue() default  "\0";
    /**
     * If the field must be deactivated when the condition is true. It will be
     * activated when the condition is false.
     * 
     * @return 
     */
    public boolean deactivate() default false;
}
