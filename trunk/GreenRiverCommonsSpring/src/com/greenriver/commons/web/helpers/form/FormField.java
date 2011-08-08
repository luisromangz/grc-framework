package com.greenriver.commons.web.helpers.form;

import java.util.Properties;

/**
 * This class models a HTML form input element.
 * @author luis
 */
public class FormField implements Comparable<FormField> {

    private String labelElement;
    private String label;
    private String widgetElement;
    private String id;

    public FormField(String id, String element, String label, String contents, Properties properties) {
        this.id = id;
        this.label = label;
        labelElement = String.format(
                "<label id='%s_label' for='%1$s'>%2$s</label>", id, label);

        String attribs = "";
        if (properties != null) {
            for (String key : properties.stringPropertyNames()) {
                attribs += String.format(" %s=\"%s\"", key,
                        properties.getProperty(key));
            }

            if (element.equals("input")) {
                widgetElement = String.format("<%s id=\"%s\"%s/><span id=\"%s\">%s</span>",
                        element, id, attribs, id+"_unit", properties.getProperty("unit"));
            } else {
                widgetElement = String.format(
                        "<%s id=\"%s\"%s>%s</%1$s><span class=\"unit\" id=\"%s\">%s</span>",
                        element, id, attribs, contents,  id+"_unit",
                        properties.getProperty("unit"));
            }
        }
    }

    /**
     * @return the widget
     */
    public String getWidgetElement() {
        return widgetElement;
    }

    public String getLabelElement() {
        return labelElement;
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

    @Override
    public int compareTo(FormField o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if(o.getClass()!=FormField.class) {
            return false;
        }

        return this.getId().equals(((FormField)o).getId());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
}
