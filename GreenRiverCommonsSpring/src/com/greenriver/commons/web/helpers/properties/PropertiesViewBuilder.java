package com.greenriver.commons.web.helpers.properties;

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
     * @param classFullName Full name of the entity
     */
    void addPropertyViewsFromClass(String classFullName);

    /**
     * Add a view for a property to the current properties view
     * @param modelClass
     */
    void addPropertyViewsFromModel(Class modelClass);   

    /**
     * Removes a property from the current properties view
     * @param id
     */
    void removePropertyView(String id);

    /**
     * Adds a generic property view with only the property name and the
     * property label value.
     * @param id
     * @param label
     * @return The created property view or null if it was not added.
     */
    SinglePropertyView addPropertyView(String id, String label);
}
