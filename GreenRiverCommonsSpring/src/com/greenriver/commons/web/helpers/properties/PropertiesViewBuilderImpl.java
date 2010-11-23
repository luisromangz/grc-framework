package com.greenriver.commons.web.helpers.properties;

import com.greenriver.commons.ClassFields;
import com.greenriver.commons.mvc.helpers.PropertyOptions;
import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.fieldProperties.EntityFieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public SinglePropertyView addPropertyView(String id,
            FieldProperties properties,
            Class modelClass) {

        if (properties == null) {
            throw new IllegalArgumentException("Properties can't be null");
        }

        if (Strings.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null nor empty.");
        }

        assertCurrent();

        if (!Strings.isNullOrEmpty(properties.accesorFieldName())) {
            id = properties.accesorFieldName();
        }

        PropertyOptions options = PropertyOptions.parseString(id);

        if (currentPropertiesView.containsPropertyViewForName(
                options.getPropName())) {
            return null;
        }

        SinglePropertyView propView =
                new SinglePropertyView(
                currentPropertiesView.getPropertyViewName(options.getPropName()));

        propView.setLabel(properties.label());

        if (setupPropertyView(propView, properties, modelClass)) {
            this.currentPropertiesView.addPropertyView(propView, options);
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
    public SinglePropertyView addPropertyView(String id, String label) {
        if (Strings.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null nor empty.");
        }

        if (label == null) {
            throw new IllegalArgumentException("Label parameter can be empty"
                    + " but not null");
        }

        assertCurrent();

        PropertyOptions options = PropertyOptions.parseString(id);

        if (currentPropertiesView.containsPropertyViewForName(
                options.getPropName())) {
            return null;
        }

        SinglePropertyView propView =
                new SinglePropertyView(
                currentPropertiesView.getPropertyViewName(options.getPropName()));

        propView.setLabel(label);
        propView.setLabelElement(
                String.format(labelFormat, propView.getId(), label));

        if (setupFieldGenericView(propView, null, null)) {
            this.currentPropertiesView.addPropertyView(propView, options);
        }

        return propView;
    }

    @Override
    public void addPropertyViewsFromClass(String entityFullName) {
        addPropertyViewsFromModel(entityFullName, null);
    }

    @Override
    public void addPropertyViewsFromModel(String entityFullName,
            List<String> propertiesToShow) {

        addPropertyViewsFromModel(
                getClassFromName(entityFullName),
                propertiesToShow);
    }

    @Override
    public void addPropertyViewsFromModel(Class modelClass) {

        addPropertyViewsFromModel(modelClass, null);
    }

    @Override
    public void addPropertyViewsFromModel(
            Class modelClass,
            List<String> propertiesToShow) {

        Field classField = null;
        FieldProperties fieldProperties = null;

        assertCurrent();

        if (Lists.isNullOrEmpty(propertiesToShow)) {
            // If the list of properties is empty we include all of them.
            propertiesToShow = generatePropertyList(modelClass);
        }

        for (String propName : propertiesToShow) {
            // Let this throw an exception if the field is not defined. This
            // looks also in the super class so if the field is not defined it
            // will throw an exception.
            classField = ClassFields.get(propName, modelClass, true, true);

            fieldProperties =
                    classField.getAnnotation(FieldProperties.class);

            //Only go ahead if there is a field property
            if (fieldProperties != null) {
                addPropertyView(propName, fieldProperties, classField.getType());
            }
        }
    }

    /**
     * Configures the views from a map.
     * @param config Map with configuration key values.
     */
    @Override
    public void addPropertyViewFromConfiguration(Map<String, Object> config) {
        if (!config.containsKey(KEY_MODEL)) {
            throw new IllegalArgumentException(
                    "The configuration must include '" + KEY_MODEL
                    + "' key with the entity full name as value");
        }

        //Temporal object to put values got from the map.
        Object obj = config.get(KEY_MODEL);

        if (obj == null || !(obj instanceof String)) {
            throw new IllegalArgumentException(
                    "The configuration must include a '" + KEY_MODEL
                    + "' key with the entity's full name as the value");
        }

        //Name of the entity's class
        String entityName = (String) obj;
        Class entityClass = getClassFromName(entityName);
        //List of properties to be added to the view.
        List<String> properties = new ArrayList<String>();
        List<String> virtualProperties = new ArrayList<String>();

        //We process the properties key if set
        if (config.containsKey(KEY_PROPERTIES)) {
            processPropertiesListObject(
                    config.get(KEY_PROPERTIES),
                    false,
                    properties);
        } else {
            properties.addAll(generatePropertyList(entityClass));
        }

        if (config.containsKey(KEY_IGNORE_PROPERTIES)) {
            processPropertiesListObject(
                    config.get(KEY_IGNORE_PROPERTIES),
                    true,
                    properties);
        }

        try {
            addPropertyViewsFromModel(entityClass, properties);
        } catch (RuntimeException rex) {
            String msg =
                    "Failed to create the properties view from configuration."
                    + " Review the properties view configuration for class "
                    + ((String)config.get(KEY_MODEL)) + " and ";
            
            if (config.containsKey(KEY_PROPERTIES)) {
                msg += "fields [" + Strings.join(properties, ", ") + "].";
            } else {
                msg += "all fields declared.";
            }

            throw new RuntimeException(msg, rex);
        }

        if (config.containsKey(KEY_VIRTUAL_PROPERTIES)) {
            processPropertiesListObject(
                    config.get(KEY_VIRTUAL_PROPERTIES),
                    false,
                    virtualProperties);
        }

        addVirtualPropertyViews(virtualProperties);
    }

    /**
     * Adds properties from a map where the keys are property names and the
     * values are the labels for the properties. The generated views will be
     * the most simple and generic ones.
     * @param virtualProperties Map with pairs propertyName-propertyLabel.
     */
    @Override
    public void addVirtualPropertyViews(List<String> virtualProperties) {
        for (String propName : virtualProperties) {
            addPropertyView(propName, "");
        }
    }

    @Override
    public void removePropertyView(String id) {
        assertCurrent();

        SinglePropertyView propView = new SinglePropertyView(
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

        EntityFieldsProperties entityProperties =
                (EntityFieldsProperties) entityClass.getAnnotation(
                EntityFieldsProperties.class);

        boolean appendSupperClassfields = entityProperties != null &&
                entityProperties.appendSuperClassFields();

        return ClassFields.getNames(
                entityClass,
                true,
                appendSupperClassfields,
                new Class[]{FieldProperties.class});
    }

    /**
     * Adds/removes all the properties specified to/from the list of properties
     * to be included in the view. The last parameter controls if the property
     * names will be added or removed from the view.
     * @param obj
     * @param ignore If true the properties are removed from the current list
     * of properties instead of being added.
     * @param result
     */
    private void processPropertiesListObject(Object obj, boolean ignore,
            List<String> result) {

        if (obj == null) {
            return;
        }

        List<String> propsList = null;

        if (obj instanceof String) {
            String[] propsArr = ((String) obj).split(";");

            if (propsArr.length > 1) {
                propsList = Arrays.asList(propsArr);
            } else if (propsArr.length == 1) {
                propsList = new ArrayList<String>();
                propsList.add(propsArr[0]);
            } else {
                throw new IllegalArgumentException("Invalid value: " + obj);
            }

        } else if (obj instanceof List) {
            propsList = (List<String>) obj;
        }

        if (ignore) {
            result.removeAll(propsList);
        } else {
            result.addAll(propsList);
        }
    }

    /**
     * Setups a property view with extra attributes. If the setup is succesful
     * this method must return true, if not this method must return false and
     * the property view will not be added to the final list and thus not
     * rendered to the interface.<br/><br/>
     * This method doesn't need to set the label, that is already done by the
     * caller of this one, but if needed it can be done here too.
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
     * Setups a generic field. The properties and modelClass parameters should
     * be allowed to be null.
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
        String unit = "";
        String label = "";

        if (properties != null) {
            if (properties.unit() != null) {
                unit = properties.unit();
            }

            if (properties.label() != null) {
                label = properties.label();
            }
        }

        //We only need to set the value here as the label should be already set
        propView.setValueElement(
                String.format(valueFormat, propView.getId(), unit));

        propView.setLabelElement(
                String.format(labelFormat, propView.getId(), label));

        return true;
    }
    //TODO: To handle more or less complex setups for other properties
    //add new methods here with the same parameters as the setupPropertiesView
    //method.
}
