package com.greenriver.commons.web.helpers.grid;

/**
 * This class holds a grid column's info.
 * @author luisro
 */
public class GridColumnInfo {
    private String field;
    private String label;
    private String width;

    public GridColumnInfo(String field) {
        this.field=field;
        this.label=field;
        width="auto";
    }
    
    

    public GridColumnInfo(String field, String label, String width) {
        this.field = field;
        this.label = label;
        this.width = width;
    }

    
    
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
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
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }
    
    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }
    
    //</editor-fold>
}
