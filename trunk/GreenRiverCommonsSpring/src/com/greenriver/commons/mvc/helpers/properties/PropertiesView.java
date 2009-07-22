
package com.greenriver.commons.mvc.helpers.properties;

import com.greenriver.commons.Strings;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a collection of properties that are the view of the properties of
 * an entity.
 *
 *
 * @author mangelp
 */
public class PropertiesView {
    //Id of the view. There is no setter as we are using the id to do the
    //equals and hashcode logic and we want it to have a valid value always.
    private String id;
    private List<SinglePropertyView> properties;

    public String getId() {
	return id;
    }

    /**
     * Gets an array with all the properties
     * @return
     */
    public SinglePropertyView[] getPropertyViews() {
	SinglePropertyView[] result = new SinglePropertyView[properties.size()];
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
	this.properties = new ArrayList<SinglePropertyView>();
    }

    public boolean addPropertyView(SinglePropertyView propView) {
	return properties.add(propView);
    }

    public boolean removePropertyView(SinglePropertyView propView) {
	return properties.remove(propView);
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
