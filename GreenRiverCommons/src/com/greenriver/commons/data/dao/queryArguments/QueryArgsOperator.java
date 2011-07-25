package com.greenriver.commons.data.dao.queryArguments;

/**
 * This enumerate type is used by the annotation QueryArgumentFieldProperties
 * to indicate which kind of comparison must take place between the annotated
 * query field and the values in the referenced field.
 *
 * @author luis
 */
public enum QueryArgsOperator {

    GREATER_THAN,
    GREATER_EQUALS,
    EQUALS,
    LOWER_EQUALS,
    LOWER_THAN,
    LIKE,
    IN_RANGE
}