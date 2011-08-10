package com.greenriver.commons.web.services;

/**
 *
 * @author luisro
 */
public class SelectOption {
    private String label;
    private Object value;
    
    public SelectOption() {
        
    }

    public SelectOption(Object value, String label) {
        this.value = value;
        this.label = label;
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
}
