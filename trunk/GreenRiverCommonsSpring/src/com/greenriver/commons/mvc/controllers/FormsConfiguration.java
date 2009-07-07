/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.mvc.controllers;

import java.util.List;

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
     * @param entityName The name of the entity a form will be created for.
     */
    void addFormEntity(String entityName);

    /**
     * Gets the names of the entities that will have a form created for edition
     * of their instances.
     * @return the formEntities
     */
    List<String> getFormEntities();

    /**
     * Sets the names of the entities a form will be created for.
     * @param formEntities the formEntities to set
     */
    void setFormEntities(List<String> formEntities);

}
