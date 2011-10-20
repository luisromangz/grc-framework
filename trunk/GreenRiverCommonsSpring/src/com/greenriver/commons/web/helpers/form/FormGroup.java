
package com.greenriver.commons.web.helpers.form;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luisro
 */
public class FormGroup {
    private Form form;
    
    private String label;
    
    
    
    private List<FormField> formFields;

    public FormGroup(Form form, String label) {
        this.form = form;
        this.label = label;
        this.formFields = new ArrayList<FormField>();
    }
       

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the formFields
     */
    public List<FormField> getFormFields() {
        return formFields;
    }

    /**
     * @param formFields the formFields to set
     */
    public void setFormFields(List<FormField> formFields) {
        this.formFields = formFields;
    }    
    
    
    public void addFormField(FormField field) {
        this.formFields.add(field);
    }

    void removeFormField(String fieldId) {
        FormField field = new FormField(this.form.getId()+"_"+fieldId, "", "", "", null);
        this.formFields.remove(field);
    }
    
}
