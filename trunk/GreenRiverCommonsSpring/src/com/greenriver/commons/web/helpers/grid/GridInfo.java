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
    private String canSortFunction = "function(idx){return true;}";

    public GridInfo(String id) {
        this.id = id;
        this.columns = new ArrayList<GridColumnInfo>();
    }

    public void addGridColumn(GridColumnInfo column) {
        this.columns.add(column);
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

    public void createCanSortFunction() {
        
        this.canSortFunction="function(idx){";
        
        this.canSortFunction+="var canSort=[";
        
        for(GridColumnInfo c : columns) {
            canSortFunction+= (c.isSortable()?"true,":"false,");
        }
        
        this.canSortFunction+="];return canSort[Math.abs(idx)];}";
        
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
     * @return the canSortFunction
     */
    public String getCanSortFunction() {
        return canSortFunction;
    }

    /**
     * @param canSortFunction the canSortFunction to set
     */
    public void setCanSortFunction(String canSortFunction) {
        this.canSortFunction = canSortFunction;
    }
    //</editor-fold>
}
