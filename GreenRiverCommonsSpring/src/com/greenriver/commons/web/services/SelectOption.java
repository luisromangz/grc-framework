package com.greenriver.commons.web.services;

/**
 *
 * @author luisro
 */
public class SelectOption implements Comparable<SelectOption> {
    private String label;
    private Object value;
    
    private String tag="";
    
    public SelectOption() {
    }

    public SelectOption(Object value, String label) {
        this();
        this.value = value;
        this.label = label;
    }

    @Override
    public int compareTo(SelectOption t) {
        return this.label.compareTo(t.getLabel());
    }
    
    

    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
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
    
    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }
    
    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    //</editor-fold>

}
