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
public @interface EntityFieldsProperties {
    /**
     * If true, the fields of the base class are added at the end, not the beggining.
     * @return
     */
    boolean appendBaseClassFields() default false;

    /**
     * A list of deactivation conditions for the fields.
     * @return
     */
    FieldDeactivationCondition[] deactivationConditions() default {};
}
