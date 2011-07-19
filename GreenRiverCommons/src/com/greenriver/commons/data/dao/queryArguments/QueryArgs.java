package com.greenriver.commons.data.dao.queryArguments;

/**
 * Common query arguments stuff
 * @author Miguel Angel
 */
public class QueryArgs {

    private int firstIndex;
    private int size;
    private String textFilter = "";
    private String sortFieldName;
    private boolean sortAscending;

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
     * @return the firstIndex
     */
    public int getFirstIndex() {
        return firstIndex;
    }

    /**
     * @param firstIndex the firstIndex to set
     */
    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
}
