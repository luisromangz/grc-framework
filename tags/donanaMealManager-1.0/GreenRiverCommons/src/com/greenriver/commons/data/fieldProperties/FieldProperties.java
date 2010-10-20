
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
     * Is the field visible in the edit form?
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
     * If true the possible values for a selection field aren't defined in this
     * annotation, if false the possible values must be defined in this
     * annotation or the property must be of type enum and have a method to
     * retrieve the values.
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
    public String enumLabelMethod() default "getName";

    /**
     * Number of decimal places to be shown if the field is of type DECIMAL.
     * @return number of decimal places
     */
    public int decimalPlaces() default 2;

    /**
     * The prefix used by the getter method.
     * @return the prefix used in the getter's name.
     */
    public String getterPrefix() default "get";

    /**
     * Minimum size of a field value. For file fields this is the file size and
     * for text fields this is the length of the string. The default value is
     * zero and negative values have no sense.
     * @return the minimum allowed size for a field value
     */
    public int minSize() default 0;
    
    /**
     * Maximum size of a field value. For file fields this is the file size.
     * @return the maximum allowed size for a field value
     */
    public int maxSize() default Integer.MAX_VALUE;
    /**
     * List of allowed file types.
     * @return an array with all the allowed file types.
     */
    public String[] allowedFileTypes() default {};

    /**
     * A field's deactivation condition.
     * @return
     */
    public FieldDeactivationCondition[] deactivationConditions() default {};

    /**
     * The desired widget size, in css format.
     * @return
     */
    public String widgetStyle() default "";

    /**
     * In case the getter or the setter arent called as the field in which the
     * annotation is applied, we can use it to tell the form builder and
     * validator.
     * 
     * @return
     */
    public String accesorFieldName() default "";

    /**
     * When the field is used in a Dojo form, is this is true, the widget will
     * emit onChange when changes are done, without waiting for the widget to lose
     * focus.
     */
    public boolean intermediateChanges() default true;
}
