/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.data.dao.queryArguments;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used in a field of a class
 * expanding EntityQueryArgument to indicate the field the value annotated
 * must be compared with, and the comparisong type.
 *
 * @author luis
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryArgsFieldProps {
    /**
     * The name of the field which value should compared with the one the
     * annotation is decorating
     * @return
     */
    public String fieldName();
    /**
     * The type of the argument's comparison.
     * @return
     */
    public QueryArgsOperator operator();
    /**
     * If its a date comparison, we should consider just the date and
     * ignore time info.
     * @return
     */
    public boolean fullDay() default true;
}