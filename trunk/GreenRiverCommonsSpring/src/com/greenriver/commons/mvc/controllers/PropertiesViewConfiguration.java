package com.greenriver.commons.mvc.controllers;

import java.util.Map;

/**
 * @author Miguel Angel
 */
public interface PropertiesViewConfiguration {

    /**
     * Adds a new properties view configuration. The id must be unique. The
     * configuration must be an string with the namespace of the entity to be
     * loaded at a minimum. It must also allow a map to be specified with the
     * following keys:
     * <ul>
     * <li><strong>model: </strong>Full name of the entity (required).</li>
     * <li><strong>properties: </strong>List of the property names to be shown (optional).</li>
     * @param id
     * @param configuration String|Map<String,Object>
     */
    void addPropertiesView(String id, Object configuration);

    /**
     * Sets the configuration for the properties view. Each value of the map
     * can be an string (full name of an entity) or a map with options to be
     * passed to the builder.<br/>
     * See <b>addPropertiesView</b> method for details.
     * @param configuration
     */
    void setPropertiesView(Map<String, Object> configuration);

    /**
     * Gets the configuration for the properties view
     * @return the map with the configuration
     */
    Map<String, Object> getPropertiesView();
}
