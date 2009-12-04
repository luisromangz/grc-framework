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
 *
 * @author luis
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryArgumentsProperties {
    /**
     * The class the query arguments are applied to.
     * @return
     */
    public Class targetClass();

    /**
     * The fields agaist the query argument's textFilter value is matched.
     * @return
     */
    public String[] textFilterFields();
    
}
