package com.greenriver.commons.data.dao.queryArgs;

/**
 * This class entities are used to represent individual field restrictions
 * for queries.
 * 
 * @author luisro
 */
public class QueryArgsRestriction {

    private String field;
    private QueryArgsOperator operator;
    private Object value;

    public QueryArgsRestriction() {
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the operator
     */
    public QueryArgsOperator getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(QueryArgsOperator operator) {
        this.operator = operator;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }
    //</editor-fold>
}
