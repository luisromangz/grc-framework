package com.greenriver.commons.mvc.helpers.form;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.fieldProperties.EntityFieldsProperties;
import com.greenriver.commons.data.fieldProperties.FieldDeactivationCondition;
import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldType;
import com.greenriver.commons.data.validation.ValidationRegex;
import com.greenriver.commons.mvc.helpers.header.HeaderConfiguration;
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
public class DojoFormBuilder implements FormBuilder, RoleManagerClient {

    private List<Form> forms;
    private Form currentForm;
    private RoleManager roleManager;
    private HeaderConfiguration configuration;

    public DojoFormBuilder() {
        forms = new ArrayList<Form>();
    }

    @Override
    public void addField(FormField field) {
        currentForm.addField(field);
    }

    @Override
    public void addField(String id, FieldProperties properties, Class fieldType) {
        String fieldId = currentForm.getId() + "_" + id;

        HtmlFormElementInfo formFieldElement = new HtmlFormElementInfo(fieldId);

        setupFieldElement(formFieldElement, fieldType, properties);

        setFieldProperties(properties, formFieldElement);

        // The field is added to the form.
        FormField field =
                new FormField(fieldId, formFieldElement.getElementType(),
                properties.label(),
                formFieldElement.getContents(),
                formFieldElement.getAttributes());

        currentForm.addField(field);

        if (properties.type() == FieldType.PASSWORDEDITOR) {
            // If the field is a password editor, then we need to add a second
            // widget so the user has to write the password twice.
            createPasswordConfirmationFormField(formFieldElement,
                    properties.label());
        }

        for (FieldDeactivationCondition condition : properties.deactivationConditions()) {
            addFieldDeactivationCondition(field.getId(), condition);
        }
    }

    public void addFieldDeactivationCondition(
            String fieldIdentifier,
            FieldDeactivationCondition condition) {

        String fieldId = null;
        if (!Strings.isNullOrEmpty(fieldIdentifier)) {
            // Real field name takes preference over the name passed in the annotation.
            fieldId = fieldIdentifier;
        } else if (!Strings.isNullOrEmpty(condition.targetField())) {
            fieldId = currentForm.getId() + "_" + condition.targetField();
        } else {
            throw new IllegalArgumentException(
                    "addFieldDeactivationCondition: A non-empty target field identifier is required");
        }


        ArrayList<String> conditions = new ArrayList<String>();
        if (!Strings.isNullOrEmpty(condition.equals())) {
            conditions.add(String.format("value==%s", condition.equals()));
        }

        if (!Strings.isNullOrEmpty(condition.notEquals())) {
            conditions.add(String.format("value!=%s", condition.notEquals()));
        }

        String conditionString = Strings.join(conditions, "||");

        String asignationStatement = "";
        if (!condition.newValue().equals("\0")) {
            asignationStatement = String.format(
                    "if(%s){widget.attr('value',%s);}",
                    conditionString,
                    condition.newValue());
        }

        String function = String.format("function(){"
                + "var value=dijit.byId('%s').attr('value');"
                + "var widget = dijit.byId('%s');"
                + "widget.setDisabled(%s);%s}",
                currentForm.getId() + "_" + condition.triggerField(),
                fieldId,
                conditionString,
                asignationStatement);

        String onChangeCode = String.format(
                "dojo.connect(dijit.byId('%s'),'onChange',%s);",
                currentForm.getId() + "_" + condition.triggerField(),
                function);

        configuration.addOnLoadScript(onChangeCode);

        String onValueSetCode = String.format(
                "dojo.connect(dijit.byId('%s'),'setValue',%s);",
                currentForm.getId() + "_" + condition.triggerField(),
                function);

        configuration.addOnLoadScript(onValueSetCode);
    }

    @Override
    public void addFieldsFromModel(Class modelClass) {
        @SuppressWarnings("unchecked")
        EntityFieldsProperties entityProperties =
                (EntityFieldsProperties) modelClass.getAnnotation(EntityFieldsProperties.class);

        if (entityProperties != null) {
            for (FieldDeactivationCondition deactivationCondition :
                    entityProperties.deactivationConditions()) {
                addFieldDeactivationCondition(null, deactivationCondition);
            }
        }

        if (entityProperties == null
                || !entityProperties.appendSuperClassFields()) {

            if (modelClass.getSuperclass() != null) {
                addFieldsFromModel(modelClass.getSuperclass());
            }
        }


        Field[] classFields = modelClass.getDeclaredFields();

        for (Field field : classFields) {
            FieldProperties props = field.getAnnotation(FieldProperties.class);
            if (props != null && props.visible()) {
                String fieldName =
                        Strings.isNullOrEmpty(props.accesorFieldName())?
                            field.getName()
                            : props.accesorFieldName();
                this.addField(fieldName, props, field.getType());
            }
        }

        if(entityProperties!=null
               && entityProperties.appendSuperClassFields()) {

            if (modelClass.getSuperclass() != null) {
                addFieldsFromModel(modelClass.getSuperclass());
            }
        }

    }

    @Override
    public void setAction(String action) {
        currentForm.setAction(action);
    }

    @Override
    public void addForm(
            String formId,
            HeaderConfiguration configuration,
            ModelAndView modelAndView) {
        Form newForm = new Form();
        newForm.setId(formId);

        modelAndView.addObject(formId, newForm);

        this.configuration = configuration;

        forms.add(newForm);
        currentForm = newForm;
    }

    @Override
    public List<Form> getForms() {
        return forms;
    }

    @Override
    public void removeField(String fieldId) {
        currentForm.removeField(fieldId);
    }

    /**
     * @return the roleManager
     */
    @Override
    public RoleManager getRoleManager() {
        return roleManager;
    }

    /**
     * @param roleManager the roleManager to set
     */
    @Override
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
                        "Error procesing element " + elementId + ". "
                        + "Enum field type is incompatible with externalValues flag");
            } else if (Strings.isNullOrEmpty(properties.enumLabelMethod())) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". "
                        + "Enum method name required but not specified");
            } else if (possibleValues.length != 0) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". "
                        + "Enum field type is incompatible with a list of possible values");
            } else if (possibleValueLabels.length != 0) {
                throw new FormBuildingException(
                        "Error procesing element " + elementId + ". "
                        + "Enum field type is incompatible with a list of possible value labels");
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

        if (!properties.externalValues()
                && (labels.size() == 0 || values.size() == 0)) {

            throw new FormBuildingException(
                    "Error procesing element " + elementId + ": "
                    + "No options specified for selection. "
                    + "Maybe you forgot to set externalValues?");

        } else if (labels.size() != values.size()) {

            throw new FormBuildingException(
                    "Error procesing element " + elementId + ": "
                    + "Number of labels doesn't match number of values");

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
        element.getAttributes().setProperty("style", "width:10em");
        element.getAttributes().setProperty("dojoType",
                "dijit.form.ValidationTextBox");
        element.getAttributes().setProperty("regExp",
                ValidationRegex.PASSWORD_ALPHA_6);
        element.getAttributes().setProperty("invalidMessage",
                "La contraseña debe tener al menos 6 carácteres y " +
                "sólo carácteres alfabéticos.");
    }

    private void setupLongTextField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);
        configuration.addDojoModule("grc.dijit.form.DowngradableTextarea");
        element.setAttribute(
                "dojoType",
                "grc.dijit.form.DowngradableTextarea");
        element.setAttribute("trim", "true");
        element.setElementType("textarea");
    }

    private void setupRichTextField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType", "dijit.Editor");
        element.setAttribute("plugins",
                "[{name:'grc.dijit._editor.plugins.FontChoice', command:'fontName', generic:false},"
                +"{name:'grc.dijit._editor.plugins.FontChoice', command:'formatBlock', generic:false},"
                +"{name:'grc.dijit._editor.plugins.FontChoice', command:'fontSize'},"
                +"'foreColor','hiliteColor',"
                +"'|','undo','redo','|','bold','italic','underline','strikethrough'," 
                +"'|', 'indent', 'outdent', 'justifyLeft', 'justifyCenter', 'justifyRight',"
                +"'|','createLink','insertImage']");
        
        element.setElementType("div");
        configuration.addDojoModule("dijit.Editor");        
        configuration.addDojoModule("dijit._editor.plugins.TextColor");
        configuration.addDojoModule("dijit._editor.plugins.LinkDialog");
        configuration.addDojoModule("grc.dijit._editor.plugins.FontChoice");
    }

    private void setupNumberField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotText(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("dojoType",
                "dijit.form.NumberSpinner");
        configuration.addDojoModule("dijit.form.NumberSpinner");

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
        configuration.addDojoModule("dijit.form.CheckBox");
    }

    private void setupEmailField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        element.getAttributes().setProperty("type", "text");
        element.getAttributes().setProperty("trim", "true");
        element.getAttributes().setProperty(
                "dojoType",
                "dijit.form.ValidationTextBox");
        element.getAttributes().setProperty(
                "regExp",
                ValidationRegex.EMAIL);
        configuration.addDojoModule("dojox.validate.regexp");
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
        configuration.addDojoModule("dojox.validate.regexp");
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

        configuration.addDojoModule("dojox.form.CheckedMultiSelect");
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
                    "Error creating selection contents for field "
                    + element.getId() + ": " + ex.getMessage());
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
        configuration.addDojoModule("dojox.widget.ColorPicker");

        String colorSelectionScript =
                String.format(
                "dojo.connect(dijit.byId('%s'),'onChange',function(newValue) {"
                + "dojo.style(dojo.byId('%1$s_colorViewer'),'background',newValue);});",
                element.getId());

        configuration.addOnLoadScript(colorSelectionScript);

        String contents = String.format(
                "<span><div id=\"%s_colorViewer\" style=\"width:1em; height:1em;"
                + " border: 1px solid #7EABCD;\"></div></span>",
                element.getId());
        contents += String.format(
                "<div dojoType=\"dijit.TooltipDialog\"><div id=\"%s\" "
                + "dojoType=\"dojox.widget.ColorPicker\" liveUpdate=\"true\"></div></div>",
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
        configuration.addDojoModule("dijit.form.NumberSpinner");

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
        element.setAttribute("ignoreCase", "false");

        try {
            element.setContents(createSelectionContents(fieldType, properties,
                    properties.possibleValues(),
                    properties.possibleValueLabels(),
                    element.getId()));
        } catch (Exception ex) {
            throw new FormBuildingException(
                    "Error creating selection contents for field "
                    + element.getId() + ": " + ex.getMessage());
        }

        configuration.addDojoModule("dijit.form.ComboBox");
    }

    private void setupRoleSelectionField(HtmlFormElementInfo element,
            Class fieldType, FieldProperties properties) {

        assertNotNumber(properties);
        assertNotText(properties);
        assertNotFile(properties);

        configuration.addDojoModule("dojox.form.CheckedMultiSelect");
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
                    "Error creating selection contents for field "
                    + element.getId() + ": " + ex.getMessage());
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
        configuration.addDojoModule("grc.dijit.form.FileInput");

        formFieldElement.setAttribute("dojoType", "grc.dijit.form.FileInput");

        if (properties.minSize() > 0) {
            constraints.add("minSize:" + properties.minSize());
        }

        if (properties.maxSize() < Integer.MAX_VALUE) {
            constraints.add("maxSize:" + properties.maxSize());
        }

        if (properties.allowedFileTypes().length > 0) {
            constraint = Strings.join(Arrays.asList(
                    properties.allowedFileTypes()), ",");
            constraints.add("types:'" + constraint + "'");
        }

        constraint = "{" + Strings.join(constraints, ",") + "}";

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


        if(!Strings.isNullOrEmpty(properties.widgetStyle())) {
            element.setAttribute("style", properties.widgetStyle());
        }

    }

    private void createPasswordConfirmationFormField(
            HtmlFormElementInfo formFieldElement, String label) {

        formFieldElement.getAttributes().setProperty("invalidMessage",
                "Las contraseñas no coinciden.");
        formFieldElement.getAttributes().remove("regExp");
        formFieldElement.getAttributes().setProperty("style", "width:10em");
        String confirmId = formFieldElement.getId() + "_confirm";
        String validationFunction =
                String.format(
                "var %s_validate = function (value) {\n"
                + "return dijit.byId(\"%s\").getValue() == value;\n" + "}",
                confirmId, formFieldElement.getId());

        String validationFunctionConnector =
                String.format(
                "dijit.byId(\'%s\').validator = %1$s_validate;",
                confirmId);

        configuration.addOnLoadScript(validationFunction);
        configuration.addOnLoadScript(
                String.format(
                "dojo.connect(dijit.byId('%s'),'onChange',function(){dijit.byId('%s').validate();});",
                formFieldElement.getId(), confirmId));

        configuration.addOnLoadScript(validationFunctionConnector);

        FormField field = new FormField(confirmId,
                formFieldElement.getElementType(),
                "Confirmar " + label.toLowerCase(),
                "", formFieldElement.getAttributes());

        currentForm.addField(field);
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
                setupMultiSelectionField(
                        formFieldElement, fieldType, properties);
                break;
            case COLOR:
                setupColorField(formFieldElement, fieldType, properties);
                break;
            case ROLESELECTOR:
                setupRoleSelectionField(
                        formFieldElement, fieldType, properties);
                break;
            case DECIMAL:
                setupDecimalField(formFieldElement, fieldType, properties);
                break;
            case AUTOCOMPLETION:
                setupAutocompletionField(
                        formFieldElement,
                        fieldType,
                        properties);
                break;
            case FILE:
                setupFileInputField(formFieldElement, fieldType, properties);
                break;
            case YEAR_DAY:
                setupYearDayField(formFieldElement, fieldType, properties);
                break;
            case TIME:
                setupTimeField(formFieldElement, fieldType, properties);
                break;
            case DATE:
                setupDateField(formFieldElement, fieldType, properties);
                break;
            case NIF:
                setupNifField(formFieldElement, fieldType, properties);
                break;
            case CIF_OR_NIF:
                setupCifOrNifField(formFieldElement, fieldType, properties);
                break;
            default:
                throw new java.lang.UnsupportedOperationException(String.format(
                        "Field tipe '%s' not supported by DojoFormBuilder",
                        properties.type().name()));
        }
    }

    private void setupYearDayField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {
        configuration.addDojoModule("grc.dijit.form.YearDayTextBox");
        formFieldElement.setAttribute("dojoType",
                "grc.dijit.form.YearDayTextBox");
    }

    private void setupTimeField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {
        configuration.addDojoModule("dijit.form.TimeTextBox");
        formFieldElement.setAttribute("dojoType", "dijit.form.TimeTextBox");
    }

    private void setupDateField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {

        configuration.addDojoModule("dojox.form.DateTextBox");
        formFieldElement.setAttribute("dojoType", "dojox.form.DateTextBox");
        // TODO: mmm, what is this for?
        formFieldElement.setAttribute("class", "dijitDateTextBox");
    }

    private void setupNifField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {
        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        configuration.addDojoModule("grc.dijit.form.CustomValidationTextBox");

        formFieldElement.setAttribute("type", "text");
        formFieldElement.setAttribute("dojoType","grc.dijit.form.CustomValidationTextBox");

        // TODO: Add validation to NIF type
        formFieldElement.setAttribute("trim", "true");
        formFieldElement.setAttribute("promptMessage", "Introduzca un NIF sin espacios u otros caracteres.");
        formFieldElement.setAttribute("invalidMessage", "NIF incorrecto.");
        // regexp to validate a nif in the form 123456789A with adition of a
        // separator (blank, - or _) between the numbers and the letter for 
        // flexibility in the input
        formFieldElement.setAttribute("validation", "nif");
        formFieldElement.setAttribute("style", "width:12em");
    }

    private void setupCifOrNifField(
            HtmlFormElementInfo formFieldElement,
            Class fieldType,
            FieldProperties properties) {
        assertNotNumber(properties);
        assertNotSelection(properties);
        assertNotFile(properties);

        configuration.addDojoModule("grc.dijit.form.CustomValidationTextBox");

        formFieldElement.setAttribute("type", "text");
        formFieldElement.setAttribute("dojoType","grc.dijit.form.CustomValidationTextBox");

        // TODO: Add validation to NIF type
        formFieldElement.setAttribute("trim", "true");
        formFieldElement.setAttribute("promptMessage", "Introduzca un CIF o NIF.");
        formFieldElement.setAttribute("invalidMessage", "CIF o NIF incorrecto.");
        // In client-side the validation is done by a custom validator 
        formFieldElement.setAttribute("validation", "cifOrNif");
        formFieldElement.setAttribute("style", "width:12em");
    }
}
