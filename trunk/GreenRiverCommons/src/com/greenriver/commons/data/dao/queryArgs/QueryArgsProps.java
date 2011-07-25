
package com.greenriver.commons.data.dao.queryArgs;

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
public @interface QueryArgsProps {
    /**
     * The fields agaist the query argument's textFilter value is matched.
     * @return
     */
    public String[] textFilterFields() default {};

    /**
     * Default fields to sort if there are no fields in this type with the
     * annotation <c>QueryArgumentsSorting</c>. If no value is set for
     * <b>defaultSortTypes</b> all the sorting will be done as ASCENDING.
     * @return
     */
    public String[] defaultSortFields() default {};
    /**
     * Default fields sorting to apply. Set to true if ascending, false if descending.
     * @return
     */
    public boolean[] defaultSortOrders() default {};
}
