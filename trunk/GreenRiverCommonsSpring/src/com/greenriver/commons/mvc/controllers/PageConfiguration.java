
package com.greenriver.commons.mvc.controllers;

import com.greenriver.commons.mvc.helpers.header.PageHeaderConfiguration;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * This class extends <c>PageHeaderConfiguration</c> in order to be able
 * to hold non-header realated page info.
 * @author luis
 */
public class PageConfiguration extends PageHeaderConfiguration
        implements FormsConfiguration {

     private Map<String,String> formEntities;

     public PageConfiguration () {
         super();

         formEntities = new Hashtable<String, String>();
     }

    /**
     * Gets the names of the entities that will have a form created for edition
     * of their instances.
     * @return the formEntities
     */
    public Map<String,String> getFormEntities() {
        return formEntities;
    }

    /**
     * Sets the names of the entities a form will be created for.
     * @param formEntities the formEntities to set
     */
    public void setFormEntities(Map<String,String> formEntities) {
        this.formEntities = new Hashtable<String, String>(formEntities);
    }

    /**
     * Adds an entity name to the list of entity names for which forms have
     * to be created.
     * @param id The form's id.
     * @param entityName The name of the entity a form will be created for.
     */
    public void addFormEntity(String id, String entityName) {
        this.formEntities.put(id, entityName);
    }
}
