package com.greenriver.commons.mvc.helpers.form;

/**
 * This interface should be implemented by those classes willing to
 * use a <c>FormBuilder</c> instance to create forms.
 * @author luis
 */
public interface FormBuilderClient {
    /**
     * Gets the <c>FormBuilder</c> instance.
     * @return The form builder.
     */
    public FormBuilder getFormBuilder();

    /**
     * Sets the <c>FormBuilder</c> instance.
     * @param formBuilder The instance to set.
     */
    public void setFormBuilder(FormBuilder formBuilder);
}
