package com.greenriver.commons.templating;

/**
 *
 * @author luisro
 */
public enum BorderType {
    NONE("Ninguno","border:none","border:none"),
    ALL("Todos","border:0.5mm solid black","border:0.5mm solid black"),
    OUTER("Exterior","border:0.5mm solid black","border:none"),
    INNER("Interiores","border: 0.5mm solid white","border: 0.5mm solid black");
    private String label;
    private String tableStyle;
    private String cellStyle;

    private BorderType(String label, String tableStyle, String cellStyle) {
        this.label = label;
        this.tableStyle = tableStyle;
        this.cellStyle = cellStyle;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the tableStyle
     */
    public String getTableStyle() {
        return tableStyle;
    }

    /**
     * @return the cellStyle
     */
    public String getCellStyle() {
        return cellStyle;
    }




}
