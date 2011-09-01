package com.greenriver.commons.data.dao.queryArgs;

/**
 * This enumerate type is used by the annotation QueryArgsRestriction
 * to indicate which kind of comparison must take place between the query 
 * field and the comparison value.
 *
 * @author luis
 */
public enum QueryArgsOperator {

    GREATER_THAN,
    GREATER_EQUALS,
    EQUALS,
    LOWER_EQUALS,
    LOWER_THAN,
    LIKE
}
