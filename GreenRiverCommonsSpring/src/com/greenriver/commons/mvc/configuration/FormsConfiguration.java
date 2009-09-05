/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.configuration;

import java.util.Map;

/**
 * This interface defines a contract that implementing classes must fulfill
 * in order to provide functionality related to include forms generated
 * automagically from an entity model in the page.
 * @author luis
 */
public interface FormsConfiguration {

    /**
     * Adds an entity name to the list of entity names for which forms have
     * to be created.
     * @param id The form's id.
     * @param entityName The name of the entity a form will be created for.
     */
    void addFormEntity(String id, String entityName);

    /**
     * Gets the names of the entities that will have a form created for edition
     * of their instances.
     * @return the formEntities
     */
    Map<String,String> getFormEntities();

    /**
     * Sets the names of the entities a form will be created for.
     * @param formEntities the formEntities to set
     */
    void setFormEntities(Map<String,String> formEntities);

}
