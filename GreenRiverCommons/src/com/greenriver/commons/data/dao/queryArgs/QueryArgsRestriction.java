package com.greenriver.commons.data.dao.queryArgs;

/**
 * This class entities are used to represent individual field restrictions
 * for queries.
 * 
 * @author luisro
 */
public class QueryArgsRestriction {

    private String field;
    private QueryArgsOp operator;
    private Object value;

    public QueryArgsRestriction() {
    }

    public QueryArgsRestriction(String field, QueryArgsOp operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof QueryArgsRestriction)) {
            return false;
        }
        
        QueryArgsRestriction oQAR = (QueryArgsRestriction)o;
        
        return field.equals(oQAR.field) && operator==oQAR.operator;        
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.field != null ? this.field.hashCode() : 0);
        hash = 97 * hash + (this.operator != null ? this.operator.hashCode() : 0);
        return hash;
    }
    
    

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the operator
     */
    public QueryArgsOp getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(QueryArgsOp operator) {
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
