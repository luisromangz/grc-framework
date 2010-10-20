
package com.greenriver.commons.data.dao.queryArguments;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation indicates that a field is used to specify ordering over
 * table fields. Then the target field must be of type string, <c>QueryArgumentSorting</c>
 * or a list of <c>QueryArgumentSorting</c>. Any other type will cause an
 * exception to be thrown.
 * @author Miguel Angel
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryArgumentsFieldOrder {
}
