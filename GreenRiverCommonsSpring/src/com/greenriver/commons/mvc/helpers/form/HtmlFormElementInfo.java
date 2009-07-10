
package com.greenriver.commons.mvc.helpers.form;

import java.util.Properties;

/**
 *
 * @author luis
 */
public class HtmlFormElementInfo {
    private String id;
    private String elementType;
    private String contents;
    private Properties attributes;

    public HtmlFormElementInfo(String fieldId) {
        id = fieldId;
        attributes = new Properties();
        elementType = "input";
        contents="";
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the element
     */
    public String getElementType() {
        return elementType;
    }

    /**
     * @param element the element to set
     */
    public void setElementType(String element) {
        this.elementType = element;
    }

    /**
     * @return the contents
     */
    public String getContents() {
        return contents;
    }

    /**
     * @param contents the contents to set
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * @return the properties
     */
    public Properties getAttributes() {
        return attributes;
    }

    /**
     * @param properties the properties to set
     */
    public void setAttributes(Properties properties) {
        this.attributes = properties;
    }
}
