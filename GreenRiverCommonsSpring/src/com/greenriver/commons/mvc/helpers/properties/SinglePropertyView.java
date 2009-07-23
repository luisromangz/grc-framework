
package com.greenriver.commons.mvc.helpers.properties;

import com.greenriver.commons.mvc.helpers.HtmlElementInfo;

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
    private String label;
    /**
     * Html element with the code for the viewing the value of the property in
     * an html document.
     */
    private String valueElement;

    public String getId() {
	return id;
    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	this.label = label;
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

    public int compareTo(SinglePropertyView o) {
	if (o == null) {
	    throw new NullPointerException("Can't compare against a null pointer");
	}

	return getId().compareTo(o.getId());
    }
    
}
