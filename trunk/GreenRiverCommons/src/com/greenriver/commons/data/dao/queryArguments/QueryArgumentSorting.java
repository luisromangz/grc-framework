
package com.greenriver.commons.data.dao.queryArguments;

/**
 * Order specification for a single field
 * @author Miguel Angel
 */
public class QueryArgumentSorting {
    private String fieldName;
    private QueryArgumentsSortType type = QueryArgumentsSortType.ASCENDING;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public QueryArgumentsSortType getType() {
        return type;
    }

    public void setType(QueryArgumentsSortType type) {
        this.type = type;
    }

    public QueryArgumentSorting() {
    }

    public QueryArgumentSorting(String fieldName, QueryArgumentsSortType type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    @Override
    public String toString() {
        return this.fieldName + " " + this.type;
    }
}
