
package com.greenriver.commons.web.helpers.properties;

import com.greenriver.commons.Strings;

/**
 * @author mangelp
 */
public class SinglePropertyView implements Comparable<SinglePropertyView> {

    /**
     * Id of this property view. Usually this is set to the id of the property
     * that this view shows.
     */
    private String id;
    /**
     * Label for this property view
     */
    @Deprecated
    private String label;
    /**
     * Html element with the code for viewing the value of the property as html.
     */
    private String valueElement;
    /**
     * Html element with the code for viewing the value of the property as html.
     */
    private String labelElement;

    public String getId() {
	return id;
    }

    @Deprecated
    public String getLabel() {
	return label;
    }

    @Deprecated
    public void setLabel(String label) {
	this.label = label;
    }

    public String getLabelElement() {
        return labelElement;
    }

    public void setLabelElement(String labelElement) {
        this.labelElement = labelElement;
    }

    /**
     * Gets the value of the property wrapped into some markup
     * @return
     */
    public String getValueElement() {
	return valueElement;
    }

    public void setValueElement(String valueElement) {
	this.valueElement = valueElement;
    }

    public SinglePropertyView(String id) {
	this.id = id;
    }

    @Override
    public int compareTo(SinglePropertyView o) {
	if (o == null) {
	    throw new NullPointerException("Can't compare against a null pointer");
	}

	return getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null ||
                !(SinglePropertyView.class.isAssignableFrom(obj.getClass()))) {
            return false;
        }

        //Two views are the same if they have the same id.
        return Strings.equals(id, ((SinglePropertyView)obj).id);
    }

    @Override
    public int hashCode() {
        String strCode = "" + id;
        return 23 + strCode.hashCode();
    }
}
