package com.greenriver.commons.web.helpers.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a grid's description.
 * @author luisro
 */
public class GridInfo {

    private String id;
    private List<GridColumnInfo> columns;
    private List<String> sortableColumns;

    public GridInfo(String id) {
        this.id = id;
        this.columns = new ArrayList<GridColumnInfo>();
        this.sortableColumns = new ArrayList<String>();
    }

    public void addGridColumn(GridColumnInfo column) {
        this.columns.add(column);
    }
    
    public void addSortableColumn(String columnField) {
        this.sortableColumns.add(columnField);
    }

    public boolean containsColumnForField(String field) {
        for (GridColumnInfo c : columns) {
            if (c.getField().equals(field)) {
                return true;
            }
        }
        return false;
    }

    public void removeGridColumn(String field) {
        for (int i = 0; i < columns.size(); i++) {
            GridColumnInfo c = columns.get(i);

            if (c.getField().equals(field)) {
                columns.remove(c);
                return;
            }
        }

        throw new IndexOutOfBoundsException(
                "Column for specified field " + field + "  not found.");
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the columns
     */
    public List<GridColumnInfo> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<GridColumnInfo> columns) {
        this.columns = columns;
    }

    /**
     * @return the sortableColumns
     */
    public List<String> getSortableColumns() {
        return sortableColumns;
    }

    /**
     * @param sortableColumns the sortableColumns to set
     */
    public void setSortableColumns(List<String> sortableColumns) {
        this.sortableColumns = sortableColumns;
    }
    //</editor-fold>
}
