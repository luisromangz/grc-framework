package com.greenriver.commons.data.dao.queryArguments;

/**
 * Common query arguments stuff
 * @author Miguel Angel
 */
public class EntityQueryArguments {

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
}
