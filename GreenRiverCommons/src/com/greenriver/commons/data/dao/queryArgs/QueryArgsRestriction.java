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
    private String value;
    
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
    public String getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    //</editor-fold>

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
}
