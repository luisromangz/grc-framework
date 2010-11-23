package com.greenriver.commons.web.helpers.form;

import java.util.ArrayList;
import java.util.List;


/**
 * This class represents a web form created using a <c>FormBuilder</c>.
 * @author luis
 */
public class Form {
    private String id;
    private String action;

    private List<FormField> fields;

    public Form() {
        fields = new ArrayList<FormField>();
    }

    /**
     * Gets the form fields.
     * @return The form fields.
     */
    public List<FormField> getFields() {
        return fields;
    }

    /**
     * Sets the for's fields.
     * @param fields The fields to set.
     */
    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }

    /**
     * Adds a field to the form.
     * @param field The field to be added.
     */
    public void addField(FormField field) {
        this.fields.add(field);
    }

    /**
     * Gets the form element id.
     * @return The form's id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the form element's id.
     * @param id The new form's id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the form's action.
     * @return The action the form will execute when submitted.
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the form's action
     * @param action The action the form will execute when submitted.
     */
    public void setAction(String action) {
        this.action = action;
    }

    void removeField(String fieldId) {
        FormField field = new FormField(this.id+"_"+fieldId, "", "", "", null);
        this.fields.remove(field);
    }
    
}
