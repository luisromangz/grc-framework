package com.greenriver.commons.data.fieldProperties;

/**
 *
 * @author luisro
 */
public @interface WidgetAction {
    String triggerValue();
    boolean equals() default true;
    String targetField();
    boolean disable() default false;
    String newValue() default "";
}
