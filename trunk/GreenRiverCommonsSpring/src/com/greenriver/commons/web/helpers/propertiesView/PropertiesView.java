
package com.greenriver.commons.web.helpers.propertiesView;

import com.greenriver.commons.Strings;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a collection of properties that are the view of the properties of
 * an entity.
 * @author Miguel Angel
 */
public class PropertiesView {
    //Id of the view. There is no setter as we are using the id to do the
    //equals and hashcode logic and we want it to have a valid value always.
    private String id;
    private List<PropertyView> properties;

    public String getId() {
	return id;
    }

    /**
     * Gets an array with all the properties
     * @return
     */
    public PropertyView[] getProperties() {
	PropertyView[] result = new PropertyView[properties.size()];
	return properties.toArray(result);
    }

    /**
     * Initiallizes the id field.
     * @param id
     */
    public PropertiesView(String id) {
	if (Strings.isNullOrEmpty(id)) {
	    throw new IllegalArgumentException("The id can't be null nor empty.");
	}
	
	this.id = id;
	this.properties = new ArrayList<PropertyView>();
    }

    /**
     * Adds a view
     * @param propView
     * @return
     */
    public boolean addPropertyView(PropertyView propView) {
	return properties.add(propView);
    }

    /**
     * Removes a view
     * @param propView
     * @return
     */
    public boolean removePropertyView(PropertyView propView) {
	return properties.remove(propView);
    }

    /**
     * Gets if a view is already added
     * @param propView
     * @return
     */
    public boolean containsView(PropertyView propView) {
        return properties.contains(propView);
    }

    /**
     * Gets if a view for a property name is already added
     * @param propName
     * @return
     */
    public boolean containsPropertyViewForName(String propName) {
        PropertyView testView =
                new PropertyView(getPropertyViewName(propName));
        return properties.contains(testView);
    }

    /**
     * Gets a name for a property view from the property name
     * @param propName
     * @return
     */
    public String getPropertyViewName(String propName) {
        return id + '_' + propName;
    }

    public int indexOfPropertyViewForName(String propName) {
        PropertyView testView =
                new PropertyView(getPropertyViewName(propName));
        return properties.indexOf(testView);
    }

    public int indexOf(PropertyView propView) {
        return properties.indexOf(propView);
    }

    public void clear() {
	properties.clear();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof PropertiesView)) {
	    return false;
	}

	return Strings.equals(id, ((PropertiesView)obj).id);
    }

    @Override
    public int hashCode() {
	return id.hashCode() + 12;
    }
}
