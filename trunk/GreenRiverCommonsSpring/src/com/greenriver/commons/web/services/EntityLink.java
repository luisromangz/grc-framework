package com.greenriver.commons.web.services;

import com.greenriver.commons.data.DataEntity;

/**
 * This class is used to return to the client an entity's label together with
 * its identifier, for use in dto's that show properties of related entities.
 * 
 * @author luisro
 */
public class EntityLink implements Comparable<EntityLink> {
    private Long id;
    private String label;
    
    public EntityLink(DataEntity entity) {
        this.id=entity.getId();
        this.label=entity.getLabel();
    }
    
     @Override
    public int compareTo(EntityLink t) {
         return label.compareTo(t.getLabel());
    }
     
    //<editor-fold defaultstate="collapsed" desc="Getters and setters">
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
    //</editor-fold>

    @Override
    public String toString() {
       return label;
    }

 
    
}
