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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsProperties {
    
    FieldsInsertionMode parentInsertionMode() default FieldsInsertionMode.APPEND;

    /**
     * A list of deactivation conditions for the fields.
     * @return
     */
    FieldDeactivationCondition[] deactivationConditions() default {};
}
