
package com.greenriver.commons.mvc.helpers.properties;

import com.greenriver.commons.mvc.helpers.PropertyOptions;
import com.greenriver.commons.mvc.helpers.PropertyOrder;
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
    private List<SinglePropertyView> properties;

    public String getId() {
	return id;
    }

    /**
     * Gets an array with all the properties
     * @return
     */
    public SinglePropertyView[] getProperties() {
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

    /**
     * Adds a view
     * @param propView
     * @return
     */
    public boolean addPropertyView(SinglePropertyView propView) {
	return addPropertyView(propView, new PropertyOptions());
    }

    /**
     * 
     * @param propView
     * @param options
     * @return
     */
    public boolean addPropertyView(SinglePropertyView propView,
            PropertyOptions options) {
        int pos = 0;

        switch(options.getOrder()) {
            case FIRST:
                return insert(propView, pos, options.getOrder());
            case LAST:
                return properties.add(propView);
            case BEFORE:
            case AFTER:
                if (!options.hasReferencePropName()) {
                    pos = -1;
                } else {
                    pos = indexOfPropertyViewForName(options.getReferencePropName());
                }

                return insert(propView, pos, options.getOrder());
            default:
                throw new UnsupportedOperationException(
                        "Not supported order " + options.getOrder());
        }
    }

    private boolean insert(SinglePropertyView propView, int pos, PropertyOrder order) {
        boolean result = true;

        if (pos < 0 || 
                (pos >= 0 &&
                    order == PropertyOrder.AFTER &&
                    properties.size() == (pos + 1))) {
            return properties.add(propView);
        }

        switch(order) {
            case BEFORE:
                properties.add(pos, propView);
                break;
            case AFTER:
                properties.add(pos + 1, propView);
                break;
            default:
                properties.add(pos, propView);
        }

        return result;
    }

    /**
     * Removes a view
     * @param propView
     * @return
     */
    public boolean removePropertyView(SinglePropertyView propView) {
	return properties.remove(propView);
    }

    /**
     * Gets if a view is already added
     * @param propView
     * @return
     */
    public boolean containsView(SinglePropertyView propView) {
        return properties.contains(propView);
    }

    /**
     * Gets if a view for a property name is already added
     * @param propName
     * @return
     */
    public boolean containsPropertyViewForName(String propName) {
        SinglePropertyView testView =
                new SinglePropertyView(getPropertyViewName(propName));
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
        SinglePropertyView testView =
                new SinglePropertyView(getPropertyViewName(propName));
        return properties.indexOf(testView);
    }

    public int indexOf(SinglePropertyView propView) {
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
