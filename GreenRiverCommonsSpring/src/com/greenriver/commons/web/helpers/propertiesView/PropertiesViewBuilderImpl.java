package com.greenriver.commons.web.helpers.propertiesView;

import com.greenriver.commons.ClassFields;
import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.FieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldProps;
import com.greenriver.commons.data.fieldProperties.FieldsInsertionMode;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Miguel Angel
 */
public class PropertiesViewBuilderImpl implements PropertiesViewBuilder {

    /**
     * Configuration key name to set the class name of the model (fully
     * qualified name).<br/>
     * The value must be a string with the fully qualified name of the class.
     */
    public static final String KEY_MODEL = "model";
    /**
     * Configuration key name to set the list of properties to be included.
     * These properties must be defined in the model. Any property not defined
     * in the model is ignored.<br/><br/>
     * Can be a list of property names, or a string of property names
     * separated with commas.
     */
    public static final String KEY_PROPERTIES = "properties";
    /**
     * Configuration key name to set a list of properties to be included but
     * that doesn't exists in the model. These properties will be generated
     * as generic properties.<br/><br/>
     * Can be a map (keys are property names and values are property labels) or
     * an string using a ; as pair separator and a = as
     * propertyName-propertyLabel separator.
     */
    public static final String KEY_VIRTUAL_PROPERTIES = "virtualProperties";
    /**
     * Configuration key name to set a list of properties to be ignored.
     * Properties listed are not used.<br/><br/>
     * Can be a list of property names, or a string of property names
     * separated with commas.
     */
    public static final String KEY_IGNORE_PROPERTIES = "ignoreProperties";
    private static final String labelFormat =
            "<span class=\"propertyViewLabelNode\" id=\"%1$s_label\">%2$s</span>";
    private static final String valueFormat = "<span class=\"propertyViewValueNode\" id=\"%1$s\"></span>"
            + "<span class=\"propertyViewUnitNode\" id=\"%1$s_unit\">%2$s</span>";
    private List<PropertiesView> propertiesViews;
    private PropertiesView currentPropertiesView;
    private Map<String, Class> classCache;

    public PropertiesViewBuilderImpl() {
        propertiesViews = new ArrayList<PropertiesView>();
        classCache = new HashMap<String, Class>();
    }

    private void assertCurrent() {
        if (currentPropertiesView == null) {
            throw new IllegalStateException(
                    "No properties view have been added to this builder");
        }
    }

    @Override
    public PropertiesView addPropertiesView(String id, ModelAndView mav) {
        if (mav.getModel().containsKey(id)) {
            throw new IllegalArgumentException(
                    "There is already a properties view with the id " + id);
        }

        currentPropertiesView = new PropertiesView(id);
        mav.addObject(id, currentPropertiesView);

        return currentPropertiesView;
    }

    @Override
    public List<PropertiesView> getPropertiesViews() {
        return Collections.unmodifiableList(propertiesViews);
    }

    @Override
    public PropertiesView getCurrentPropertiesView() {
        return currentPropertiesView;
    }

    @Override
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

    private PropertyView addPropertyView(
            String id, FieldProps properties, Class modelClass) {

        if (properties == null) {
            throw new IllegalArgumentException("Properties can't be null");
        }

        if (Strings.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null nor empty.");
        }

        assertCurrent();

        if (currentPropertiesView.containsPropertyViewForName(id)) {
            return null;
        }

        PropertyView propView =
                new PropertyView(
                currentPropertiesView.getPropertyViewName(id));

        propView.setLabelElement(properties.label());

        if (setupPropertyView(propView)) {
            this.currentPropertiesView.addPropertyView(propView);
        }

        return propView;
    }

    /**
     * Adds a generic property view with only the property name and the
     * property label value.
     * @param id
     * @param label Label for the element. Can be an empty string but not null.
     * @return The created property view or null if it was not added.
     */
    @Override
    public PropertyView addPropertyView(String id, String label) {
        if (Strings.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null nor empty.");
        }

        if (label == null) {
            throw new IllegalArgumentException("Label parameter can be empty"
                    + " but not null");
        }

        assertCurrent();

        if (currentPropertiesView.containsPropertyViewForName(id)) {
            return null;
        }

        PropertyView propView =
                new PropertyView(
                currentPropertiesView.getPropertyViewName(id));

        propView.setLabelElement(label);
        propView.setLabelElement(
                String.format(labelFormat, propView.getId(), label));

        if (setupPropertyView(propView)) {
            this.currentPropertiesView.addPropertyView(propView);
        }

        return propView;
    }

    @Override
    public void addPropertiesViewFromClass(String classFullName) {
        addPropertiesViewFromClass(getClassFromName(classFullName));
    }
  

    @Override
    public void addPropertiesViewFromClass(Class viewClass) {

        assertCurrent();

        // If the list of properties is empty we include all of them.
        List<String>  propertiesToShow = generatePropertyList(viewClass);

        for (String propName : propertiesToShow) {
            // Let this throw an exception if the field is not defined. This
            // looks also in the super class so if the field is not defined it
            // will throw an exception.
            Field classField = ClassFields.get(propName, viewClass, true, true);

            FieldProps fieldProps = classField.getAnnotation(FieldProps.class);

            //Only go ahead if there is a field property
            if (fieldProps != null) {
                addPropertyView(propName, fieldProps, classField.getType());
            }
        }
    }   

    @Override
    public void removePropertyView(String id) {
        assertCurrent();

        PropertyView propView = new PropertyView(
                currentPropertiesView.getId() + "_" + id);
        currentPropertiesView.removePropertyView(propView);
    }

    private Class getClassFromName(String classFullName) {
        if (classCache.containsKey(classFullName)) {
            return classCache.get(classFullName);
        }

        Class result = null;

        try {
            result = Class.forName(classFullName);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException(
                    "Entity class '" + classFullName + "' can't be loaded.",
                    ex);
        }

        classCache.put(classFullName, result);

        return result;
    }

    /**
     * Generates a list with all the annotated properties in the class
     * (The annotation to find is FieldProperties) and all its super classes.
     * @param entityClass
     * @return a list of properties
     */
    private List<String> generatePropertyList(Class entityClass) {

        if (entityClass == null) {
            throw new NullPointerException("Parameter entityClass is null.");
        }

        if (entityClass == Object.class) {
            return new ArrayList<String>(0);
        }

        FieldsProperties entityProperties =
                (FieldsProperties) entityClass.getAnnotation(
                FieldsProperties.class);

        return ClassFields.getNames(
                entityClass,
                true,
                entityProperties==null?FieldsInsertionMode.NONE: entityProperties.parentInsertionMode(),
                new Class[]{FieldProps.class});
    }

   
    /**
     * Setups a property view with extra attributes. If the setup is succesful
     * this method must return true, if not this method must return false and
     * the property view will not be added to the final list and thus not
     * rendered to the interface.<br/><br/>
     * This method doesn't need to set the label, that is already done by the
     * caller of this one, but if needed it can be done here too.
     * @param propView   
     * @return true if the propertyView must be added or false if it must be
     * discarded.
     */
    private boolean setupPropertyView(PropertyView propView) {

         //all the view are the same, an span for the value and another one
        //for the unit
        String unit = "";
        String label = propView.getLabelElement();

        //We only need to set the value here as the label should be already set
        propView.setValueElement(
                String.format(valueFormat, propView.getId(), unit));

        propView.setLabelElement(
                String.format(labelFormat, propView.getId(), label));

        return true;
    }
}
