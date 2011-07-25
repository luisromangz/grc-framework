package com.greenriver.commons.data.dao.queryArgs;

/**
 * This class entities are used to represent individual field restrictions
 * for queries.
 * 
 * @author luisro
 */
public class QueryArgsRestriction {
    private String fieldName;
    private QueryArgsOperator operator;
    private Object value;

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }
    
    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
    
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
    //</editor-fold>
}
