package com.greenriver.commons.web.configuration;

import java.util.Map;

/**
 * Operations that allow to configure a view of the properties of a entity from
 * the app model.
 * @author Miguel Angel
 */
public interface PropertiesViewContainer {

    /**
     * Adds a new properties view configuration. The id must be unique. The
     * configuration must be an string with the namespace of the entity to be
     * loaded at a minimum.
     */
    void addPropertiesView(String id, String className);

    /**
     * Sets the configuration for the properties view. Each value of the map
     * can be an string (full name of an entity) or a map with options to be
     * passed to the builder.<br/>
     * See <b>addPropertiesView</b> method for details.
     * @param configuration
     */
    void setPropertiesView(Map<String, String> propertiesView);

    /**
     * Gets the configuration for the properties view
     * @return the map with the configuration
     */
    Map<String, String> getPropertiesView();
}
