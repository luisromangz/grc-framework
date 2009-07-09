package com.greenriver.commons.mvc.helpers.form;

import com.greenriver.commons.data.fieldProperties.FieldProperties;
import com.greenriver.commons.data.fieldProperties.FieldPropertiesValidator;
import com.greenriver.commons.data.fieldProperties.FieldType;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurer;
import com.greenriver.commons.mvc.helpers.header.HeaderConfigurerClient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 * This class implements a form creator that will create
 * Dojo-aware widget info.
 * @author luis
 */
public class DojoFormBuilder implements FormBuilder, HeaderConfigurerClient {

    private List<Form> forms;
    private Form lastForm;

    public DojoFormBuilder() {
        forms = new ArrayList<Form>();
    }

    public void addField(FormField field) {
        lastForm.addField(field);
    }

    public void addField(String id, FieldProperties properties, Class fieldType) {


        String fieldId = lastForm.getId() + "_" + id;

        Properties props = new Properties();
        String elementType = "input";
        String contents = "";

        if (properties.required()) {
            props.setProperty("required", "true");
        }


        props.setProperty("intermediateChanges", "true");
        props.setProperty("unit", properties.unit());

        switch (properties.type()) {
            case TEXT:
                props.setProperty("type", "text");
                props.setProperty("dojoType", "dijit.form.ValidationTextBox");
                props.setProperty("trim", "true");
                break;
            case PASSWORD:
            case PASSWORDEDITOR:
                props.setProperty("type", "password");
                props.setProperty("dojoType", "dijit.form.ValidationTextBox");
                props.setProperty("regExp",
                        FieldPropertiesValidator.PASSWORD_PATTERN.pattern());
                props.setProperty("invalidMessage",
                        "La contraseña debe tener al menos 6 carácteres.");
                break;
            case LONGTEXT:
                props.setProperty("dojoType", "dijit.form.Textarea");
                elementType = "textarea";
                break;
            case RICHTEXT:
                props.setProperty("dojoType", "dijit.form.Editor");
                elementType = "textarea";
                headerConfigurer.addDojoModule("dijit.form.Editor");
                break;
            case NUMBER:
                props.setProperty("dojoType", "dijit.form.NumberSpinner");
                headerConfigurer.addDojoModule("dijit.form.NumberSpinner");
                break;
            case BOOLEAN:
                props.setProperty("dojoType", "dijit.form.CheckBox");
                props.setProperty("type", "checkbox");
                headerConfigurer.addDojoModule("dijit.form.CheckBox");
                break;
            case EMAIL:
                props.setProperty("type", "text");
                props.setProperty("dojoType", "dijit.form.ValidationTextBox");
                break;
            case IPADDRESS:
                props.setProperty("type", "text");
                props.setProperty("regExpGen", "dojox.regexp.ipAddress");
                props.setProperty("dojoType", "dijit.form.ValidationTextBox");
                props.setProperty("constraints",
                        "{allowIPv6:false,allowHybrid:false, allowDecimal:false}");
                props.setProperty("invalidMessage", "Dirección IP inválida.");
                headerConfigurer.addDojoModule("dojox.validate.regexp");
                break;
            case SELECTION:
                props.setProperty("dojoType", "dijit.form.FilteringSelect");
                elementType = "select";

                contents = createSelectionContents(fieldType, properties);

                break;
            case MULTISELECTION:
                headerConfigurer.addDojoModule("dojox.form.CheckedMultiSelect");
                props.setProperty("dojoType", "dojox.form.CheckedMultiSelect");
                props.setProperty("multiple", "true");
                elementType = "select";

                contents = createSelectionContents(fieldType, properties);
                break;
            case COLOR:
                elementType = "div";
                props.setProperty("dojoType", "dijit.form.DropDownButton");
                headerConfigurer.addDojoModule("dojox.widget.ColorPicker");

                contents +=
                        "<span><div id=\"" + fieldId + "_colorViewer\" style=\"width:1em; height:1em; border: 1px solid #7EABCD;\"></div></span>";
                contents +=
                        "<div dojoType=\"dijit.TooltipDialog\">" + "<div id=\"" + fieldId + "\" dojoType=\"dojox.widget.ColorPicker\" liveUpdate=\"true\"></div>" + "</div>";

                String colorSelectionScript =
                        String.format(
                        "dojo.connect(dijit.byId('%s'),'onChange',function(newValue) {dojo.style(dojo.byId('%1$s_colorViewer'),'background',newValue);});",
                        fieldId);

                headerConfigurer.addOnLoadScript(colorSelectionScript);


                fieldId = fieldId + "_dropDownButton";
                break;
        }

        // Custom options are set now.
        if (!properties.invalidMessage().isEmpty()) {
            props.setProperty("invalidMessage", properties.invalidMessage());
        }

        if (!properties.rangeMessage().isEmpty()) {
            props.setProperty("rangeMessage", properties.rangeMessage());
        }

        if (!properties.customRegExp().isEmpty()) {
            props.setProperty("regExp", properties.customRegExp());
        }

        if (!properties.editable()) {
            props.setProperty("disabled", "true");
        }


        props.setProperty(
                "constraints",
                String.format("{min: %s, max: %s}", properties.minValue(),
                properties.maxValue()));


        // The field is added to the form.
        FormField field =
                new FormField(fieldId, elementType, properties.label(), contents,
                props);

        lastForm.addField(field);

        if (properties.type() == FieldType.PASSWORDEDITOR) {
            // If the field is a password editor, then we need to add a second
            // widget so the user has to write the password twice.

            props.setProperty("invalidMessage", "Las contraseñas no coinciden.");
            props.remove("regExp");
            String confirmId = fieldId + "_confirm";
            String validationFunction =
                    String.format(
                    "var %s_validate = function (value) {\n"
                    + "return dijit.byId(\"%s\").getValue() == value;\n"
                    +"}",
                    confirmId, fieldId);

            String validationFunctionConnector =
                    String.format(
                    "dijit.byId(\'%s\').validator = %1$s_validate;",
                    confirmId);

            headerConfigurer.addScript(validationFunction);
            headerConfigurer.addOnLoadScript(
                    String.format(
                    "dojo.connect(dijit.byId('%s'),'onChange',function(){dijit.byId('%s').validate();});",
                    fieldId, confirmId));

            headerConfigurer.addOnLoadScript(validationFunctionConnector);

            field = new FormField(confirmId, elementType,
                    "Confirmar " + properties.label().toLowerCase(),
                    contents, props);

            lastForm.addField(field);
        }
    }

    public void addFieldsFromModel(Class modelClass) {
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
    private HeaderConfigurer headerConfigurer;

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

    private String createSelectionContents(Class fieldType, FieldProperties properties ) {

        String contents = "";
        ArrayList<String> values = new ArrayList<String>();
        ArrayList<String> labels = new ArrayList<String>();

        if (fieldType.isEnum()) {
            // We have to extract the values from the Enum;

            for (Object constant : fieldType.getEnumConstants()) {
                try {

                    String name =
                            (String) constant.getClass().getDeclaredMethod(
                            "getName").invoke(constant);

                    values.add(constant.toString());
                    labels.add(name);
                } catch (Exception ex) {
                    Logger.getLogger(DojoFormBuilder.class.getName()).log(
                            Level.SEVERE,
                            null, ex);
                }
            }

        } else {

            values.addAll(Arrays.asList(properties.possibleValues()));
            labels.addAll(Arrays.asList(properties.possibleValueLabels()));

            if (values.size() != labels.size()) {
                values = labels;
            }
        }

        for (int i = 0; i < labels.size(); i++) {
            contents +=
                    String.format("<option value=\"%s\">%s</option>",
                    values.get(i), labels.get(i));
        }

        return contents;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void removeField(String fieldId) {
        lastForm.removeField(fieldId);
    }
}
