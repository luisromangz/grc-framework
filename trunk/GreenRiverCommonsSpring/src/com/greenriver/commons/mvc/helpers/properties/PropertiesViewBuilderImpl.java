package com.greenriver.commons.mvc.helpers.properties;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.collections.Maps;
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

        assertCurrent();

        if (currentPropertiesView.containsPropertyViewForName(id)) {
            return null;
        }

        SinglePropertyView propView = new SinglePropertyView(
                currentPropertiesView.getPropertyViewName(id));

        propView.setLabel(properties.label());

        if (setupPropertyView(propView, properties, modelClass)) {
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
    public SinglePropertyView addPropertyView(String id, String label) {
        if (Strings.isNullOrEmpty(id)) {
            throw new IllegalArgumentException("Id can't be null nor empty.");
        }

        if (label == null) {
            throw new IllegalArgumentException("Label parameter can be empty" +
                    " but not null");
        }

        assertCurrent();

        if (currentPropertiesView.containsPropertyViewForName(id)) {
            return null;
        }

        SinglePropertyView propView = new SinglePropertyView(
                currentPropertiesView.getPropertyViewName(id));

        propView.setLabel(label);

        if (setupFieldGenericView(propView, null, null)) {
            this.currentPropertiesView.addPropertyView(propView);
        }

        return propView;
    }

    public void addPropertyViewsFromModel(String entityFullName) {
        addPropertyViewsFromModel(entityFullName, null);
    }

    public void addPropertyViewsFromModel(String entityFullName, 
            List<String> propertiesToShow) {
        
        addPropertyViewsFromModel(
                getClassFromName(entityFullName),
                propertiesToShow);
    }

    public void addPropertyViewsFromModel(Class modelClass) {
        addPropertyViewsFromModel(modelClass, null);
    }

    public void addPropertyViewsFromModel(Class modelClass,
            List<String> propertiesToShow) {

        Field classField = null;
        FieldProperties fieldProperties = null;

        assertCurrent();

        if (Lists.isNullOrEmpty(propertiesToShow)) {
            propertiesToShow = generatePropertyList(modelClass);
        }

        for (String propName : propertiesToShow) {
            try {
                classField = modelClass.getDeclaredField(propName);
            } catch (NoSuchFieldException ex) {
                throw new IllegalArgumentException("Field '" + propName + 
                        "' not defined in model entity " + modelClass.getName());
            }

            fieldProperties = classField.getAnnotation(FieldProperties.class);

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
    public void addPropertyViewFromConfiguration(Map<String, Object> config) {
        if (!config.containsKey(KEY_MODEL)) {
            throw new IllegalArgumentException(
                    "The configuration must include '" + KEY_MODEL +
                    "' key with the entity full name as value");
        }

        //Temporal object to put values got from the map.
        Object obj = config.get(KEY_MODEL);
        //Name of the entity's class
        String entityName = null;
        Class entityClass = null;
        //List of properties to be added to the view.
        List<String> properties = new ArrayList<String>();
        Map<String, String> virtualProperties = new HashMap<String, String>();

        if (obj == null || !(obj instanceof String)) {
            throw new IllegalArgumentException(
                    "The configuration must include '" + KEY_MODEL +
                    "' key with the entity full name as value");
        }

        entityName = (String) obj;
        entityClass = getClassFromName(entityName);
        
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

        addPropertyViewsFromModel(entityClass, properties);

        if (config.containsKey(KEY_VIRTUAL_PROPERTIES)) {
            processPropertiesMapObject(
                    config.get(KEY_VIRTUAL_PROPERTIES),
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
    public void addVirtualPropertyViews(Map<String, String> virtualProperties) {
        for (String propName : virtualProperties.keySet()) {
            addPropertyView(propName, virtualProperties.get(propName));
        }
    }

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
     * Generates a list with all the annotated properties in the class.
     * (The annotation to find is FieldProperties).
     * @param entityClass
     * @return a list of properties
     */
    private List<String> generatePropertyList(Class entityClass) {
        
        Field[] classFields = entityClass.getDeclaredFields();
        List<String> result = new ArrayList<String>();

        for (Field field : classFields) {
            FieldProperties props = field.getAnnotation(FieldProperties.class);

            if (props != null) {
                // We only add the field if was annotated
                result.add(field.getName());
            }
        }

        return result;
    }

    private void processPropertiesMapObject(Object obj,
            Map<String, String> result) {
        if (obj == null) {
            return;
        }

        Map<String, String> propMap = new HashMap<String, String>(0);

        if (obj instanceof String) {
            propMap = Maps.fromString(((String) obj), ";", "=");
        } else if (obj instanceof Map) {
            propMap = (Map<String, String>) obj;
        } else {
            throw new IllegalArgumentException("Invalid value format. " +
                    "Can't hangle type " + obj.getClass());
        }

        result.putAll(propMap);
    }

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
        String format = "<span id=\"%1$s\"></span>" +
                "<span style=\"margin-left:5px\" id=\"%1$s_unit\">%2$s</span>";
        String unit = "";

        if (properties != null) {
            if (properties.unit() != null) {
                unit = properties.unit();
            }
        }

        //We only need to set the value here as the label should be already set
        propView.setValueElement(
                String.format(format, propView.getId(), unit));

        return true;
    }
    
    //TODO: To handle more or less complex setups for other properties
    //add new methods here with the same parameters as the setupPropertiesView
    //method.
}
