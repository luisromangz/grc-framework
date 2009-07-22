package com.greenriver.commons.mvc.helpers;

import com.greenriver.commons.Strings;
import java.util.Properties;

/**
 * Hmtl element with an unique id. Two elements are the same if the id is the
 * same (equals implementation).
 */
public class HtmlElementInfo {

    private Properties attributes = null;
    private String contents = "";
    private String elementType = null;
    private String id = null;

    /**
     * @return the properties
     */
    public Properties getAttributes() {
	return attributes;
    }

    /**
     * @return the contents
     */
    public String getContents() {
	return contents;
    }

    /**
     * @return the element
     */
    public String getElementType() {
	return elementType;
    }

    /**
     * @return the id
     */
    public String getId() {
	return id;
    }

    protected void setId(String id) {
	this.id = id;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(String contents) {
	this.contents = contents;
    }

    /**
     * @param element the element to set
     */
    public void setElementType(String element) {
	this.elementType = element;
    }

    public HtmlElementInfo(String id) {
	this.id = id;
	attributes = new Properties();
    }

    /**
     * Sets the value of an attribute
     * @param name
     * @param value
     */
    public void setAttribute(String name, String value) {
	if (Strings.isNullOrEmpty(name)) {
	    throw new IllegalArgumentException(
		    "The name can't be null or empty");
	}
	
	if (name.equalsIgnoreCase("id")) {
	    throw new IllegalArgumentException(
		    "The id property can't be set.");
	}
	
	attributes.put(name, value);
    }

    public void toString(StringBuilder sb) {
	sb.append(String.format("<%s id=\"%s\"", elementType.toLowerCase(), getId()));
	
	for (String key : attributes.stringPropertyNames()) {
	    if (key.equalsIgnoreCase("id")) {
		continue;
	    }
	    
	    sb.append(String.format(" %s=\"%s\"", key,
		    attributes.getProperty(key)));
	}
	
	sb.append(">");
	sb.append(getContents());
	sb.append("</" + elementType.toLowerCase() + ">");
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder();
	toString(sb);
	return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof HtmlElementInfo)) {
	    return false;
	}

	return Strings.equals(id, ((HtmlElementInfo)obj).id);
    }

    @Override
    public int hashCode() {
	return super.hashCode();
    }
}
