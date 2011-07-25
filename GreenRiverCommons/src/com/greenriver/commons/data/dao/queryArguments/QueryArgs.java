package com.greenriver.commons.data.dao.queryArguments;

/**
 * Common query arguments stuff
 * @author Miguel Angel
 */
public class QueryArgs {

    private int first;
    private int count;
    private String textFilter = "";
    private String sortFieldName;
    private boolean sortAscending;

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

    
    
    //</editor-fold>

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
}
