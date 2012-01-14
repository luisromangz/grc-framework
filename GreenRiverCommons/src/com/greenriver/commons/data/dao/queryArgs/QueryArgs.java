package com.greenriver.commons.data.dao.queryArgs;

import java.util.ArrayList;
import java.util.List;

/**
 * This class instances are used to pass queries to the server.
 * 
 * @author Miguel Angel
 */
public class QueryArgs {

    private int first;
    private int count;
    private String textFilter = "";
    private String sortFieldName;
    private boolean sortAscending;
    
    private List<QueryArgsRestriction> restrictions;

    public QueryArgs() {
        restrictions = new ArrayList<QueryArgsRestriction>();
    }    
    
    public void addRestriction(QueryArgsRestriction queryArgsRestriction) {
        restrictions.add(queryArgsRestriction);
    }
    
    public void addRestriction(String field, QueryArgsOperator op, Object value) {
        restrictions.add(new QueryArgsRestriction(field,op, value));
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the fieldName
     */
    public String getSortFieldName() {
        return sortFieldName;
    }
    
    /**
     * @param fieldName the fieldName to set
     */
    public void setSortFieldName(String fieldName) {
        this.sortFieldName = fieldName;
    }
    
    /**
     * @return the ascending
     */
    public boolean isSortAscending() {
        return sortAscending;
    }
    
    /**
     * @param ascending the ascending to set
     */
    public void setSortAscending(boolean ascending) {
        this.sortAscending = ascending;
    }
    
    /**
     * @return the textFilter
     */
    public String getTextFilter() {
        return textFilter;
    }
    
    /**
     * @param textFilter the textFilter to set
     */
    public void setTextFilter(String textFilter) {
        this.textFilter = textFilter.trim();
    }

    
    
    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the first
     */
    public int getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(int first) {
        this.first = first;
    }
    
    /**
     * @return the restrictions
     */
    public List<QueryArgsRestriction> getRestrictions() {
        return restrictions;
    }

    /**
     * @param restrictions the restrictions to set
     */
    public void setRestrictions(List<QueryArgsRestriction> restrictions) {
        this.restrictions = restrictions;
    }
    
    //</editor-fold>

   

}
