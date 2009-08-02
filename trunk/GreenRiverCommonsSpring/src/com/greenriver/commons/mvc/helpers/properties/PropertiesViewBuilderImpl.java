package com.greenriver.commons.mvc.helpers.properties;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Miguel Angel
 */
public class PropertiesViewBuilderImpl implements PropertiesViewBuilder {

    private static final String key_model = "model";
    private static final String key_properties = "properties";
    private List<PropertiesView> propertiesViews;
    private PropertiesView currentPropertiesView;

    public PropertiesViewBuilderImpl() {
        propertiesViews = new ArrayList<PropertiesView>();
    }

    private void assertCurrent() {
        if (currentPropertiesView == null) {
            throw new IllegalStateException(
                    "No properties view have been added to this builder");
        }
    }

    public PropertiesView addPropertiesView(String id, ModelAndView mav) {
        if (mav.getModel().containsKey(id)) {
            throw new IllegalArgumentException(
                    "There is already a properties view with the id " + id);
        }

        currentPropertiesView = new PropertiesView(id);
        mav.addObject(id, currentPropertiesView);

        return currentPropertiesView;
    }

    public List<PropertiesView> getPropertiesViews() {
        return Collections.unmodifiableList(propertiesViews);
    }

    public PropertiesView getCurrentPropertiesView() {
        return currentPropertiesView;
    }

    public void makeCurrent(PropertiesView propertiesView) {
        if (propertiesView == null) {
            throw new IllegalArgumentException(
                    "The properties view can't be null");
        }

        if (!propertiesViews.contains(propertiesView)) {
            throw new IllegalArgumentException(
                    "The properties view is not defined in the builder");
        }

        this.currentPropertiesView = propertiesView;
    }

    public SinglePropertyView addPropertyView(String id,
            FieldProperties properties,
            Class modelClass) {

        if (properties == null) {
            throw new IllegalArgumentException("Properties can't be null");
        }

        if (Strings.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null nor empty.");
        }

        //If the property is not visible don't add nothing.
        if (!properties.visible()) {
            return null;
        }

        assertCurrent();

        SinglePropertyView propView = new SinglePropertyView(
                this.currentPropertiesView.getId() + "_" + id);
        propView.setLabel(properties.label());

        if (setupPropertyView(propView, properties, modelClass)) {
            this.currentPropertiesView.addPropertyView(propView);
        }

        return propView;
    }

    public void addPropertyViewsFromModel(String entityFullName) {
        addPropertyViewsFromModel(entityFullName, null);
    }

    public void addPropertyViewsFromModel(String entityFullName, List<String> propertiesToShow) {
        Class modelClass = null;

        try {
            modelClass = Class.forName(entityFullName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                    "Entity class '" + entityFullName + "' can't be loaded.",
                    ex);
        }

        addPropertyViewsFromModel(modelClass, propertiesToShow);
    }

    public void addPropertyViewsFromModel(Class modelClass) {
        addPropertyViewsFromModel(modelClass, null);
    }

    public void addPropertyViewsFromModel(Class modelClass, List<String> propertiesToShow) {
        assertCurrent();

        Field[] classFields = modelClass.getDeclaredFields();

        for (Field field : classFields) {
            //If there is a list of properties we only are going through those
            //properties in the list.
            if (propertiesToShow != null &&
                    !propertiesToShow.contains(field.getName())) {
                continue;
            }

            FieldProperties props = field.getAnnotation(FieldProperties.class);
            if (props != null && props.visible()) {
                // We only add the field if was annotated and the property is
                //marked as visible.
                this.addPropertyView(field.getName(), props, field.getType());
            }
        }
    }

    public void addPropertyViewFromConfiguration(Map<String, Object> config) {
        if (!config.containsKey(key_model)) {
            throw new IllegalArgumentException(
                    "The configuration must include '" + key_model +
                    "' key with the entity full name as value");
        }

        Object obj = config.get(key_model);
        String entityName = null;
        List<String> properties = null;

        if (obj == null || !(obj instanceof String)) {
            throw new IllegalArgumentException(
                    "The configuration must include '" + key_model +
                    "' key with the entity full name as value");
        }

        entityName = (String) obj;
        obj = config.get(key_properties);

        if (obj != null && !(obj instanceof List)) {
            throw new IllegalArgumentException(
                    "The configuration includes key '" + key_properties +
                    "' but the value is not a list.");
        } else if (obj != null) {
            properties = (List<String>) obj;
            addPropertyViewsFromModel(entityName, properties);
        } else {
            addPropertyViewsFromModel(entityName);
        }
    }

    public void removePropertyView(String id) {
        assertCurrent();

        SinglePropertyView propView = new SinglePropertyView(
                currentPropertiesView.getId() + "_" + id);
        currentPropertiesView.removePropertyView(propView);
    }

    /**
     * Setups a property view with extra attributes. If the setup is succesful
     * this method must return true, if not this method must return false and
     * the property view will not be added to the final list and thus not
     * rendered to the interface.
     * @param propView
     * @param properties
     * @param modelClass
     * @return true if the propertyView must be added or false if it must be
     * discarded.
     */
    private boolean setupPropertyView(SinglePropertyView propView,
            FieldProperties properties, Class modelClass) {

        switch (properties.type()) {
            //TODO: Concrete initiallization for anybody?
            default:
                return setupFieldGenericView(propView, properties, modelClass);
        }
    }

    /**
     * Setups a generic field
     * @param propView
     * @param properties
     * @param modelClass
     * @return
     */
    private boolean setupFieldGenericView(
            SinglePropertyView propView,
            FieldProperties properties,
            Class modelClass) {
        //all the view are the same, an span for the value and another one
        //for the unit
        String format = "<span id=\"%1$s\"></span>";
        format +=
                "<span style=\"margin-left:5px\" id=\"%1$s_unit\">%2$s</span>";

        String unit = properties.unit();
        //If no unit put an empty string
        if (unit == null) {
            unit = "";
        }

        propView.setValueElement(
                String.format(format, propView.getId(), unit));

        return true;
    }
    //TODO: To handle more or less complex setups for other properties
    //add new methods here with the same parameters as the setupPropertiesView
    //method.
}
