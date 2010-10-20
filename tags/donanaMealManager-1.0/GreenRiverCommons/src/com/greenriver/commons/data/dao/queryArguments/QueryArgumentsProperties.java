
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
    public String[] textFilterFields() default {};

    /**
     * Default fields to sort if there are no fields in this type with the
     * annotation <c>QueryArgumentsSorting</c>. If no value is set for
     * <b>defaultSortTypes</b> all the sorting will be done as ASCENDING.
     * @return
     */
    public String[] defaultSortFields() default {};
    /**
     * Default fields sorting to apply if there are no fields in this type
     * with the annotation <c>QueryArgumentsSorting</c>
     * @return
     */
    public QueryArgumentsSortType[] defaultSortTypes() default {};
}
