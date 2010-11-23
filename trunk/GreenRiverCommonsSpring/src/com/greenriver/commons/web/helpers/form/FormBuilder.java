package com.greenriver.commons.web.helpers.form;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.mvc.helpers.header.HeaderConfig;
import java.util.List;
import org.springframework.web.servlet.ModelAndView;

/**
 * This interface define the contract a form builder should implement.
 * @author luis
 */
public interface FormBuilder {

    /**
     * Adds a form to the form collection, making it the form being edited
     * e. g. the form fields are being added to.
     * @param formId The new form's id.
     * @param configuration The configuration object used to hold javascript files etc.
     * @param modelAndView The <c>ModelAndView</c> object the form will be added to.
     */
    public void addForm(
            String formId,
            HeaderConfig configuration,
            ModelAndView modelAndView);

    /**
     * Gets the created forms.
     * @return A <c>List</c> of forms.
     */
    public List<Form> getForms();

    /**
     * Sets the action of the form currently being edited.
     * @param action The action the form currently being edited should perform
     * when submited.
     */
    public void setAction(String action);

    /**
     * Adds a field to the form currently being edited.
     * @param field The field added.
     */
    public void addField(FormField field);

    /**
     * Adds a field to the form currently being edited.
     * @param id The new field's id.
     * @param properties The field properties.
     * @param fieldType The type of the field to be added.
     */
    public void addField(String id, FieldProperties properties, Class fieldType);

    /**
     * Adds a set of fields dinamically using annotations given to a class'
     * properties.
     * @param modelClass The class holding the properties that will be scanned
     * for info about fields.
     */
    public void addFieldsFromClass(Class modelClass);

    public void removeField(String fieldId);
}
