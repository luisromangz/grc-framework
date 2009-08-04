package com.greenriver.commons.mvc.controllers;

import java.util.Map;

/**
 * @author Miguel Angel
 */
public interface PropertiesViewConfiguration {

    /**
     * Adds a new properties view configuration. The id must be unique. The
     * configuration must be an string with the namespace of the entity to be
     * loaded at a minimum. <br/>
     * It must also allow a map to be specified instead of an string. The
     * available configuration keys are:
     * <ul>
     * <li><strong>model (required): </strong>
     * Fully qualified name of the class of the entiy.</li>
     * <li><strong>properties (optional): </strong>
     * List of the property names to be shown. Also accepts a string with the
     * property names to show separated by commas.</li>
     * <li><strong>ignoredProperties (optional): </strong>
     * List of the property names to be ignored from those that are set with a
     * notation. Also accepts a string with the property names to show
     * separated by commas.</li>
     * <li><strong>virtualProperties (optional): </strong>
     * Map of propertyName-propertyLabel pairs to generate property views of
     * properties that didn't really exists, but that may be helpfull when
     * customizing a view. It also accepts a string using ';' as pairs separator
     * and '=' as the separator between the property name and the label.</li>
     * </ul>
     * <br/>
     * When configuring the list of properties to show the ignoredProperties
     * are processed after the properties setting, so if you add a list of 
     * properties to show you can add a setting to ignore some of them, but the
     * way to use this is to either specify what to ignore or what to show.
     * If no properties are specified the list of properties is created from
     * the model.<br/><br/>
     * When a property view is found to be duplicated it must not be added and
     * the implemenation must continue without throwing any exceptions.<br/>
     * If all the properties of the model are ignored (or all specified
     * properties are later ignored) all the properties from the model will be
     * shown.<br/>
     * Any configuration key not listed here must be simply ignored.
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
