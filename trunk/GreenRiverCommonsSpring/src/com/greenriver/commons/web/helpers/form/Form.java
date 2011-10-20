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
    
    private List<FormGroup> fieldGroups;

    public Form() {
        fieldGroups = new ArrayList<FormGroup>();
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

    /**
     * @return the fieldGroups
     */
    public List<FormGroup> getFieldGroups() {
        return fieldGroups;
    }

    /**
     * @param fieldGroups the fieldGroups to set
     */
    public void setFieldGroups(List<FormGroup> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }
    
    public void addFieldGroup(FormGroup group) {
        fieldGroups.add(group);
    }

    void removeFormField(String fieldId) {
        for(FormGroup group : fieldGroups) {
            group.removeFormField(fieldId);
        }
    }
}
