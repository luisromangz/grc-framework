
package com.greenriver.commons.data.fieldProperties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation mark fields with info with columns properties so a grid can 
 * be generated.
 * 
 * @author luisro
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GridColumn {
    /**
     * The width of the grid column.
     * @return 
     */
    String width() default "auto";
    
    /**
     * If true, we will be able to sort by this column.
     * @return 
     */
    boolean canSort() default true;
}
