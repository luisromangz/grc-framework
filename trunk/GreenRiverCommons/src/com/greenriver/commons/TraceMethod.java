/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 *
 * @author MiguelAngel
 */
@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TraceMethod {

}
