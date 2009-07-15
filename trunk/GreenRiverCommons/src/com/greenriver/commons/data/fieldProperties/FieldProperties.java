/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.fieldProperties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation provides support for detailing metadata for the model's
 * fields.
 * @author luis
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldProperties {

    /**
     * Is the field visible in the GUI?
     * @return
     */
    public boolean visible() default true;


    
    /**
     * The field's associated label.
     * @return
     */
    String label();
    /**
     * The field's descrition.
     * @return
     */
    String description() default "";
    /**
     * Tells if the field is required or not.
     * @return
     */
    boolean required() default true;

    /**
     * A custom regular expresion used to validate the field.
     * @return
     */
    String customRegExp() default "";

    /**
     * The minimum numeric value the field allows. Only for DECIMAL or NUMBER
     * field types.
     * @return 
     */
    double minValue() default Integer.MIN_VALUE;

    /**
     * The max numeric value the field allows. Only for DECIMAL or NUMBER field
     * types.
     * @return
     */
    double maxValue() default Integer.MAX_VALUE;
    
    /**
     * The field's type, that will determinate input type and the validation
     * performed.
     * @return
     */
    FieldType type() default FieldType.TEXT;
    /**
     * The possible options that should accept the field.
     * @return
     */
    String[] possibleValues() default {};

    /**
     * The possible labels for the options accepted by the field.
     * @return
     */
    String[] possibleValueLabels() default {};


    /**
     * The possible values for a selection field aren't defined here but
     * somewhere else.
     * @return
     */
    boolean externalValues() default false;

    /**
     * A custom validation error message.
     * @return
     */
    String invalidMessage() default "";

    /**
     * A custom range error message.
     * @return
     */
    String rangeMessage() default "";

    /**
     * Tells if the field is editable (in the UI).
     * @return
     */
    boolean editable() default true;


    /**
     * The unit the field values is measured in.
     * @return
     */
    public String unit() default "";

    /**
     * Name of the method of an enum that returns the map of label/value to be
     * used to allow editing of a field with enum type.
     * @return the name of the method
     */
    public String fromEnumMethod() default "getName";

    /**
     * Number of decimal places to be shown if the field is of type DECIMAL.
     */
    public int decimalPlaces() default 3;
}
