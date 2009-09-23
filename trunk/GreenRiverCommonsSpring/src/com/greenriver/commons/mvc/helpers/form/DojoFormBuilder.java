package com.greenriver.commons.mvc.helpers.form;

import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldPropertiesValidator;
import com.greenriver.commons.data.fieldProperties.FieldType;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurerClient;
import com.greenriver.commons.roleManagement.RoleManager;
import com.greenriver.commons.roleManagement.RoleManagerClient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a form creator that will create Dojo-aware widget info.
 * Note that this class should be configured in spring with a request scope as
 * it has state.
 * @author luis
 */
public class DojoFormBuilder implements FormBuilder, HeaderConfigurerClient,
        RoleManagerClient {

    private List<Form> forms;
    private Form lastForm;
    private RoleManager roleManager;
    private HeaderConfigurer headerConfigurer;

    public DojoFormBuilder() {
        forms = new ArrayList<Form>();
    }

    public void addField(FormField field) {
        lastForm.addField(field);
    }

    public void addField(String id, FieldProperties properties, Class fieldType) {
        String fieldId = lastForm.getId() + "_" + id;

        HtmlFormElementInfo formFieldElement = new HtmlFormElementInfo(fieldId);

        setupFieldElement(formFieldElement, fieldType, properties);

        setFieldProperties(properties, formFieldElement);

        // The field is added to the form.
        FormField field =
                new FormField(fieldId, formFieldElement.getElementType(),
                properties.label(),
                formFieldElement.getContents(),
                formFieldElement.getAttributes());

        lastForm.addField(field);

        if (properties.type() == FieldType.PASSWORDEDITOR) {
            // If the field is a password editor, then we need to add a second
            // widget so the user has to write the password twice.
            createPasswordConfirmationFormField(formFieldElement,
                    properties.label());
        }
    }

    public void addFieldsFromModel(Class modelClass) {
        if (modelClass.getSuperclass() != null) {
            addFieldsFromModel(modelClass.getSuperclass());
        }


        Field[] classFields = modelClass.getDeclaredFields();

        for (Field field : classFields) {
            FieldProperties props = field.getAnnotation(FieldProperties.class);
            if (props != null && props.visible()) {
                this.addField(field.getName(), props, field.getType());
            }
        }


    }

    public void setAction(String action) {
        lastForm.setAction(action);
    }

    public void setHeaderConfigurer(HeaderConfigurer headerConfigurer) {
        this.headerConfigurer = headerConfigurer;
    }

    public HeaderConfigurer getHeaderConfigurer() {
        return this.headerConfigurer;
    }

    public void addForm(String formId, ModelAndView modelAndView) {
        Form newForm = new Form();
        newForm.setId(formId);

        modelAndView.addObject(formId, newForm);

        forms.add(newForm);
        lastForm = newForm;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void removeField(String fieldId) {
        lastForm.removeField(fieldId);
    }

    /**
     * @return the roleManager
     */
    public RoleManager getRoleManager() {
        return roleManager;
    }

    /**
     * @param roleManager the roleManager to set
     */
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    private String createSelectionContents(Class fieldType, FieldProperties properties,
            String[] possibleValues, String[] possibleValueLabels, String elementId) {

        String contents = "";
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> labels = new ArrayList<String>();

        if (fieldType.isEnum()) {

            if (properties.externalValues()) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". " +
                        "Enum field type is incompatible with externalValues flag");
            } else if (Strings.isNullOrEmpty(properties.enumLabelMethod())) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". " +
                        "Enum method name required but not specified");
            } else if (possibleValues.length != 0) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". " +
                        "Enum field type is incompatible with a list of possible values");
            } else if (possibleValueLabels.length != 0) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". " +
                        "Enum field type is incompatible with a list of possible value labels");
            }

            // We have to extract the values from the Enum;
            for (Object constant : fieldType.getEnumConstants()) {
                try {
                    String name = (String) constant.getClass().getDeclaredMethod(
                            properties.enumLabelMethod()).invoke(constant);

                    values.add(constant.toString());
                    labels.add(name);
                } catch (Exception ex) {
                    Logger.getLogger(DojoFormBuilder.class.getName()).log(
                            Level.SEVERE,
                            null, ex);
                }
            }

        } else if (!properties.externalValues()) {

            values.addAll(Arrays.asList(possibleValues));
            labels.addAll(Arrays.asList(possibleValueLabels));

            if (values.size() != labels.size()) {
                values = labels;
            }
        }

        if (!properties.externalValues() &&
                (labels.size() == 0 || values.size() == 0)) {

            throw new FormBuildingException(
                    "Error procesing element " + elementId + ": " +
                    "No options specified for selection. " +
                    "Maybe you forgot to set externalValues?");

        } else if (labels.size() != values.size()) {

            throw new FormBuildingException(
                    "Error procesing element " + elementId + ": " +
                    "Number of labels doesn't match number of values");

        }

        for (int i = 0; i < labels.size(); i++) {
            contents +=
                    String.format("<option value=\"%s\">%s</option>",
                    values.get(i), labels.get(i));
        }

        return contents;
    }

    /**
     * Asserts that no numeric-related parameters are changed from default
     * values. These parameters are minValue, maxValue and decimalPlaces.
     */
    private void assertNotNumber(FieldProperties properties) {
        if (properties.minValue() != -Double.MAX_VALUE) {
            throw new FormBuildingException(
                    "Min value specified but property is not a number.");
        }

        if (properties.maxValue() != Double.MAX_VALUE) {
            throw new FormBuildingException(
                    "Min value specified but property is not a number.");
        }

        if (!"".equals(properties.rangeMessage())) {
            throw new FormBuildingException(
                    "A range error message have been specified but property is not a number.");
        }
    }

    /**
     * Asserts that no text-related parameters are changed from default values.
     * The only parameter is customRegExp.
     * @param properties
     */
    private void assertNotText(FieldProperties properties) {
        if (!"".equals(properties.customRegExp())) {
            throw new FormBuildingException(
                    "Custom regular expression specified but property is not a text.");
        }
    }

    /**
     * Asserts that no selection-related parameters are changed from default
     * values. These parameters are enumLabelMethod, possibleValueLabels and
     * possibleValues.
     * @param properties
     */
    private void assertNotSelection(FieldProperties properties) {
        if (!"getName".equals(properties.enumLabelMethod())) {
            throw new FormBuildingException(
                    "Enum method name specified but property is not a selection.");
        }

        if (properties.possibleValueLabels().length != 0) {
            throw new FormBuildingException(
                    "Posible value labels specified but property is not a selection.");
        }

        if (properties.possibleValues().length != 0) {
            throw new FormBuildingException(
                    "Posible values specified but property is not a selection.");
        }
    }

    /**
     * Asserts that properties specific to file properties are not set for
     * other types of properties.
     * @param properties
     */
    private void assertNotFile(FieldProperties properties) {
        if (properties.allowedFileTypes().length != 0) {
            throw new FormBuildingException(
                    "Allowed file types specified but property is not a file.");
        }

        if (properties.minSize() != 0) {
            throw new FormBuildingException(
                    "Minimum size set but property is not a file.");
        }

        if (properties.maxSize() != Integer.MAX_VALUE) {
            throw new FormBuildingException(
                    "Maximum size set but property is not a file.");
        }
    }

    private void setupTextField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("type", "text");
        element.getAttributes().setProperty("dojoType",
                "dijit.form.ValidationTextBox");
        element.getAttributes().setProperty("trim", "true");
    }

    private void setupPasswordField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("type", "password");
        element.getAttributes().setProperty("dojoType",
                "dijit.form.ValidationTextBox");
        element.getAttributes().setProperty("regExp",
                FieldPropertiesValidator.PASSWORD_PATTERN.pattern());
        element.getAttributes().setProperty("invalidMessage",
                "La contraseña debe tener al menos 6 carácteres.");
    }

    private void setupLongTextField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);
        headerConfigurer.addDojoModule("grc.dijit.form.DowngradableTextarea");
        element.setAttribute(
                "dojoType",
                "grc.dijit.form.DowngradableTextarea");
        element.setElementType("textarea");
        element.setAttribute("cols", "25");
        element.setAttribute("style", "width:auto");
    }

    private void setupRichTextField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType", "dijit.form.Editor");
        element.setElementType("textarea");
        headerConfigurer.addDojoModule("dijit.form.Editor");
    }

    private void setupNumberField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotText(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType",
                "dijit.form.NumberSpinner");
        headerConfigurer.addDojoModule("dijit.form.NumberSpinner");

        String min = properties.minValue() + "";
        String max = properties.maxValue() + "";

        element.getAttributes().setProperty(
                "constraints",
                String.format("{min: %s, max: %s, places:0}", min, max));
    }

    private void setupBooleanField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotText(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType", "dijit.form.CheckBox");
        element.getAttributes().setProperty("type", "checkbox");
        headerConfigurer.addDojoModule("dijit.form.CheckBox");
    }

    private void setupEmailField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("type", "text");
        element.getAttributes().setProperty(
                "dojoType",
                "dijit.form.ValidationTextBox");
        element.getAttributes().setProperty(
                "regExp",
                "[\\w\\d._%+-]+@[\\w\\d.-]+\\.[\\w]{2,6}");
        headerConfigurer.addDojoModule("dojox.validate.regexp");
    }

    private void setupIpAddressField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("type", "text");
        element.getAttributes().setProperty("regExpGen",
                "dojox.regexp.ipAddress");
        element.getAttributes().setProperty("dojoType",
                "dijit.form.ValidationTextBox");
        element.getAttributes().setProperty("constraints",
                "{allowIPv6:false,allowHybrid:false, allowDecimal:false}");
        element.getAttributes().setProperty("invalidMessage",
                "Dirección IP inválida.");
        headerConfigurer.addDojoModule("dojox.validate.regexp");
    }

    private void setupSelectionField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotText(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType",
                "dijit.form.FilteringSelect");
        element.setElementType("select");

        element.setContents(createSelectionContents(fieldType, properties,
                properties.possibleValues(),
                properties.possibleValueLabels(),
                element.getId()));
    }

    private void setupMultiSelectionField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotText(properties);
        assertNotFile(properties);

        headerConfigurer.addDojoModule("dojox.form.CheckedMultiSelect");
        element.getAttributes().setProperty("dojoType",
                "dojox.form.CheckedMultiSelect");
        element.getAttributes().setProperty("multiple", "true");
        element.setElementType("select");

        try {
            element.setContents(createSelectionContents(fieldType, properties,
                    properties.possibleValues(),
                    properties.possibleValueLabels(),
                    element.getId()));
        } catch (Exception ex) {
            throw new FormBuildingException(
                    "Error creating selection contents for field " + element.getId() + ": " + ex.getMessage());
        }
    }

    private void setupColorField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotText(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.setElementType("div");
        element.getAttributes().setProperty(
                "dojoType", "dijit.form.DropDownButton");
        headerConfigurer.addDojoModule("dojox.widget.ColorPicker");

        String colorSelectionScript =
                String.format(
                "dojo.connect(dijit.byId('%s'),'onChange',function(newValue) {dojo.style(dojo.byId('%1$s_colorViewer'),'background',newValue);});",
                element.getId());

        headerConfigurer.addOnLoadScript(colorSelectionScript);

        String contents = String.format(
                "<span><div id=\"%s_colorViewer\" style=\"width:1em; height:1em; border: 1px solid #7EABCD;\"></div></span>",
                element.getId());
        contents += String.format(
                "<div dojoType=\"dijit.TooltipDialog\"><div id=\"%s\" dojoType=\"dojox.widget.ColorPicker\" liveUpdate=\"true\"></div></div>",
                element.getId());

        element.setContents(contents);
        element.setId(element.getId() + "_dropDownButton");
    }

    private void setupDecimalField(HtmlFormElementInfo element,
            Class FieldType, FieldProperties properties) {

        assertNotText(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType",
                "dijit.form.NumberSpinner");
        headerConfigurer.addDojoModule("dijit.form.NumberSpinner");

        //Here we set the range, note that if min or max are float it must
        //use a dot as decimal separator (representation independent in javascript).
        String min = properties.minValue() + "";
        String max = properties.maxValue() + "";
        String places = properties.decimalPlaces() + "";

        element.getAttributes().setProperty(
                "constraints",
                String.format("{min: %s, max: %s, places:%s}", min, max, places));
    }

    private void setupAutocompletionField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType",
                "dijit.form.ComboBox");
        element.setElementType("select");
        element.getAttributes().setProperty("class",
                "backgroundlessComboBoxButton");

        try {
            element.setContents(createSelectionContents(fieldType, properties,
                    properties.possibleValues(),
                    properties.possibleValueLabels(),
                    element.getId()));
        } catch (Exception ex) {
            throw new FormBuildingException(
                    "Error creating selection contents for field " + element.getId() + ": " + ex.getMessage());
        }

        headerConfigurer.addDojoModule("dijit.form.ComboBox");
    }

    private void setupRoleSelectionField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotText(properties);
        assertNotFile(properties);

        headerConfigurer.addDojoModule("dojox.form.CheckedMultiSelect");
        element.getAttributes().setProperty("dojoType",
                "dojox.form.CheckedMultiSelect");
        element.getAttributes().setProperty("multiple", "true");
        element.setElementType("select");

        try {
            element.setContents(createSelectionContents(fieldType, properties,
                    roleManager.getRoleNames(),
                    roleManager.getRoleLabels(),
                    element.getId()));
        } catch (Exception ex) {
            throw new FormBuildingException(
                    "Error creating selection contents for field " + element.getId() + ": " + ex.getMessage());
        }
    }

    private void setupFileInputField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotText(properties);

        List<String> constraints = new ArrayList<String>();
        String constraint = null;

        // We import dojo file input's replacement.
        headerConfigurer.addDojoModule("grc.dijit.form.FileInput");

        formFieldElement.setAttribute("dojoType", "grc.dijit.form.FileInput");

        if (properties.minSize() > 0) {
            constraints.add("minSize:" + properties.minSize());
        }

        if (properties.maxSize() < Integer.MAX_VALUE) {
            constraints.add("maxSize:" + properties.maxSize());
        }

        if (properties.allowedFileTypes().length > 0) {
            constraint = Lists.join(Arrays.asList(
                    properties.allowedFileTypes()), ",");
            constraints.add("types:'" + constraint + "'");
        }

        constraint = "{" + Lists.join(constraints, ",") + "}";

        formFieldElement.setAttribute("constraints", constraint);
    }

    /**
     * Sets generic properties for a field. Handle custom properties in each
     * field concrete setup.
     * @param properties
     * @param element
     */
    private void setFieldProperties(FieldProperties properties,
            HtmlFormElementInfo element) {
        if (properties.required()) {
            element.getAttributes().setProperty("required", "true");
        }

        element.getAttributes().setProperty("intermediateChanges", "true");
        element.getAttributes().setProperty("unit", properties.unit());

        // Custom options are set now.
        if (!properties.invalidMessage().isEmpty()) {
            element.getAttributes().setProperty("invalidMessage",
                    properties.invalidMessage());
        }

        if (!properties.rangeMessage().isEmpty()) {
            element.getAttributes().setProperty("rangeMessage",
                    properties.rangeMessage());
        }

        if (!properties.customRegExp().isEmpty()) {
            element.getAttributes().setProperty("regExp",
                    properties.customRegExp());
        }

        if (!properties.editable()) {
            element.getAttributes().setProperty("disabled", "true");
        }
    }

    private void createPasswordConfirmationFormField(
            HtmlFormElementInfo formFieldElement, String label) {

        formFieldElement.getAttributes().setProperty("invalidMessage",
                "Las contraseñas no coinciden.");
        formFieldElement.getAttributes().remove("regExp");
        String confirmId = formFieldElement.getId() + "_confirm";
        String validationFunction =
                String.format(
                "var %s_validate = function (value) {\n" + "return dijit.byId(\"%s\").getValue() == value;\n" + "}",
                confirmId, formFieldElement.getId());

        String validationFunctionConnector =
                String.format(
                "dijit.byId(\'%s\').validator = %1$s_validate;",
                confirmId);

        headerConfigurer.addScript(validationFunction);
        headerConfigurer.addOnLoadScript(
                String.format(
                "dojo.connect(dijit.byId('%s'),'onChange',function(){dijit.byId('%s').validate();});",
                formFieldElement.getId(), confirmId));

        headerConfigurer.addOnLoadScript(validationFunctionConnector);

        FormField field = new FormField(confirmId,
                formFieldElement.getElementType(),
                "Confirmar " + label.toLowerCase(),
                "", formFieldElement.getAttributes());

        lastForm.addField(field);
    }

    private void setupFieldElement(HtmlFormElementInfo formFieldElement,
            Class fieldType, FieldProperties properties) {

        switch (properties.type()) {
            case TEXT:
                setupTextField(formFieldElement, fieldType, properties);
                break;
            case PASSWORD:
            case PASSWORDEDITOR:
                setupPasswordField(formFieldElement, fieldType, properties);
                break;
            case LONGTEXT:
                setupLongTextField(formFieldElement, fieldType, properties);
                break;
            case RICHTEXT:
                setupRichTextField(formFieldElement, fieldType, properties);
                break;
            case NUMBER:
                setupNumberField(formFieldElement, fieldType, properties);
                break;
            case BOOLEAN:
                setupBooleanField(formFieldElement, fieldType, properties);
                break;
            case EMAIL:
                setupEmailField(formFieldElement, fieldType, properties);
                break;
            case IPADDRESS:
                setupIpAddressField(formFieldElement, fieldType, properties);
                break;
            case SELECTION:
                setupSelectionField(formFieldElement, fieldType, properties);
                break;
            case MULTISELECTION:
                setupMultiSelectionField(formFieldElement, fieldType, properties);
                break;
            case COLOR:
                setupColorField(formFieldElement, fieldType, properties);
                break;
            case ROLESELECTOR:
                setupRoleSelectionField(formFieldElement, fieldType, properties);
                break;
            case DECIMAL:
                setupDecimalField(formFieldElement, fieldType, properties);
                break;
            case AUTOCOMPLETION:
                setupAutocompletionField(formFieldElement, fieldType, properties);
                break;
            case FILE:
                setupFileInputField(formFieldElement, fieldType, properties);
                break;
            case YEAR_DAY:
                setupYearDayField(formFieldElement, fieldType, properties);
                break;
            case TIME:
                setupTimeField(formFieldElement, fieldType, properties);
        }
    }

    private void setupYearDayField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {
        headerConfigurer.addDojoModule("grc.dijit.form.YearDayTextBox");
        formFieldElement.setAttribute("dojoType",
                "grc.dijit.form.YearDayTextBox");
    }

    private void setupTimeField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {
        headerConfigurer.addDojoModule("dijit.form.TimeTextBox");
        formFieldElement.setAttribute("dojoType", "dijit.form.TimeTextBox");
    }
}
