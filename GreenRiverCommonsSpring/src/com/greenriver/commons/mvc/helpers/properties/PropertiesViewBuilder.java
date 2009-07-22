package com.greenriver.commons.mvc.helpers.properties;

import com.greenriver.commons.data.fieldProperties.FieldProperties;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Miguel Angel
 */
public interface PropertiesViewBuilder {

    /**
     * Adds a properties view to the ModelAndView instance. The properties view
     * is created, and if another with the same name exists an exception is
     * thrown. This PropertiesView is made the current one.
     * 
     * @param id Id of the properties view to create.
     * @param mav Instance where the properties view should be added with
     * the id specified.
     * @return The newly created PropertiesView instance.
     */
    PropertiesView addPropertiesView(String id, ModelAndView mav);

    /**
     * Gets all the defined properties views.
     * @return
     */
    List<PropertiesView> getPropertiesViews();

    /**
     * Gets the current Properties view
     * @return
     */
    PropertiesView getCurrentPropertiesView();

    /**
     * Changes the current properties view. If the properties view is not in
     * the builder this method throws an exception
     * @param propertiesView
     * @throws IllegalArgumentException if the properties view is not
     * defined in the builder.
     */
    void makeCurrent(PropertiesView propertiesView);

    /**
     * Add a view for a property to the current properties view
     * @param id
     * @param properties
     * @param fieldType
     * @return the property view added
     */
    SinglePropertyView addPropertyView(String id, FieldProperties properties, Class fieldType);

    /**
     * Add a view for a property to the current properties view
     * @param entityFullName Full name of the entity
     */
    void addPropertyViewsFromModel(String entityFullName);

    /**
     * Add a view for a property to the current properties view
     * @param entityFullName Full name of the entity
     * @param propertiesToShow
     */
    void addPropertyViewsFromModel(String entityFullName, List<String> propertiesToShow);

    /**
     * Add a view for a property to the current properties view
     * @param modelClass
     * @param propertiesToShow 
     */
    void addPropertyViewsFromModel(Class modelClass, List<String> propertiesToShow);

    /**
     * Add a view for a property to the current properties view
     * @param modelClass
     */
    void addPropertyViewsFromModel(Class modelClass);

    /**
     * Adds a view for the properties of an entity configured in a map. The
     * map should contain the keys as described in javadoc of
     * <b>PropertiesViewConfiguration.addPropertiesViewConfiguration</b>.
     * @param config
     */
    void addPropertyViewFromConfiguration(Map<String, Object> config);

    /**
     * Removes a property from the current properties view
     * @param id
     */
    void removePropertyView(String id);
}
