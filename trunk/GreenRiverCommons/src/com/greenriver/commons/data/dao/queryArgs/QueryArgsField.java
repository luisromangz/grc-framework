package com.greenriver.commons.data.dao.queryArgs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used in a field of a class
 * annotated with QueryArgsProps to indicate the field will be avalaible
 * as a query restriction.
 *
 * @author luis
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryArgsField {
 
}