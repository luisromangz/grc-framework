package com.greenriver.commons.data.fieldProperties;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.data.validation.ValidationRegex;
import com.greenriver.commons.data.validation.ValidationResult;
import com.greenriver.commons.roleManagement.RoleManager;
import com.greenriver.commons.validators.CIFOrNIFValidator;
import com.greenriver.commons.validators.CIFValidator;
import com.greenriver.commons.validators.EmailValidator;
import com.greenriver.commons.validators.NIFValidator;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 * Validates widget fields.
 *
 * @author luis
 */
public class WidgetFieldsValidator implements FieldsValidator {

    public static final Pattern COLOR_PATTERN = Pattern.compile(
            "^" + ValidationRegex.COLOR + "$",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" + ValidationRegex.PASSWORD_ALPHA_6 + "$");
    private RoleManager roleManager;
    private EmailValidator emailValidator;

    @Override
    public ValidationResult validate(Object object) {
        ValidationResult result = new ValidationResult();

        if (object == null) {
            result.addErrorMessage("El objeto recibido es nulo.");
            result.setValid(false);
            return result;
        }

        validateFieldsByClass(object, object.getClass(), result);

        return result;
    }

    private void validateFieldsByClass(
            Object object,
            Class validationClass,
            ValidationResult result) {
        // We want to validate the fields defined in a class superclass also.
        if (validationClass.getSuperclass() != null) {
            validateFieldsByClass(object, validationClass.getSuperclass(), result);
        }

        Field[] fields = validationClass.getDeclaredFields();
        for (Field field : fields) {
            WidgetProps properties = field.getAnnotation(
                    WidgetProps.class);
            if (properties != null) {
                // The field has WidgetProps annotation, so it can be validated.

                List<String> fieldErrorMessages = this.validateField(
                        field,
                        properties,
                        object);
                if (!fieldErrorMessages.isEmpty()) {
                    result.addErrorMessages(fieldErrorMessages);
                }
            }
        }
    }

    private List<String> validateField(
            Field field,
            WidgetProps properties,
            Object object) {
        List<String> validationMessages = new ArrayList<String>();

        if (!(properties.visible() && properties.editable())) {
            // There wasn't user input, so there is nothing to validate.
            return validationMessages;
        }

        String methodName = field.getName();
        if (!Strings.isNullOrEmpty(properties.accesorFieldName())) {
            methodName = properties.accesorFieldName();
        }

        // We compose the name of the accesor;
        methodName = Strings.toUpperCase(methodName, 0, 1);
        if (!Strings.isNullOrEmpty(properties.getterPrefix())) {
            // We add the prefix.
            methodName = properties.getterPrefix() + methodName;
        } else if (field.getType().getName().equals("boolean")) {
            methodName = "is" + methodName;
        } else {
            methodName = "get" + methodName;
        }

        Object value = null;
        try {
            value = object.getClass().getMethod(methodName).invoke(object);
        } catch (Exception ex) {
            validationMessages.add(String.format(
                    "Hubo un problema al recuperar el valor de un campo: %s.",
                    properties.label()));
            return validationMessages;
        }

        if (properties.required() && value == null) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» es obligatorio.",
                    properties.label()));
        } else if (value != null) {
            validateFieldType(value, properties, validationMessages);
        }

        return validationMessages;
    }

    @Override
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    public RoleManager getRoleManager() {
        return this.roleManager;
    }

    private void validateMultiSelection(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        validateMultiSelectorHelper(value, properties, validationMessages,
                properties.possibleValues());

    }

    private void validateText(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        String text = (String) value;
        if (properties.required() && Strings.isNullOrEmpty(text)) {
            validationMessages.add(String.format(
                    "Es necesario rellenar el campo «%s».",
                    properties.label()));
            return;
        } else if (!properties.required() && Strings.isNullOrEmpty(text)) {
            // We don't have to validate this.
            return;
        }

        if (!properties.customRegExp().isEmpty()) {
            Pattern textPattern = Pattern.compile(
                    properties.customRegExp());
            CharSequence seq =
                    text.subSequence(0, text.length());
            if (!textPattern.matcher(seq).matches()) {
                validationMessages.add(
                        "El campo «" + properties.label() + "» no tiene un formato válido.");
                return;
            }
        }

        if (properties.minSize() > text.length() && properties.maxSize() < text.length()) {
            validationMessages.add(String.format(
                    "El campo «%0$s» tiene que tener un tamaño entre %1$d y %1$d caracteres.",
                    properties.label(),
                    properties.minSize(),
                    properties.maxSize()));
        } else if (properties.minSize() > text.length()) {
            validationMessages.add(String.format(
                    "El campo «%0$s» tiene que tener un tamaño mayor o igual a %1$d caracteres.",
                    properties.label(),
                    properties.minSize()));
        } else if (properties.minSize() > text.length()) {
            validationMessages.add(String.format(
                    "El campo «%0$s» tiene que tener un tamaño menor o igual a %1$d caracteres.",
                    properties.label(),
                    properties.maxSize()));
        }
    }

    private void validateColor(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        String colorValue = (String) value;

        if (properties.required() && colorValue.isEmpty()) {
            validationMessages.add(String.format(
                    "Es necesario rellenar el campo «%s».",
                    properties.label()));
            return;
        } else if (!properties.required() && Strings.isNullOrEmpty(
                colorValue)) {
            // We don't have to validate this.
            return;
        }

        if (!COLOR_PATTERN.matcher((CharSequence) colorValue).matches()) {
            validationMessages.add(
                    "El campo «" + properties.label() + "» no tiene formato de color RGB hexadecimal.");
        }
    }

    private void validateNumber(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        // We parse the number so we don't need to deal with
        // diferent integer types.
        Integer number = Integer.parseInt(value.toString());


        if (properties.maxValue() < number) {
            validationMessages.add(
                    "El valor del campo «" + properties.label() + "» supera el máximo permitido (" + properties.maxValue() + ").");
        }

        if (properties.minValue() > number) {
            validationMessages.add(
                    "El valor del campo «" + properties.label() + "» es menor que el mímimo permitido (" + properties.minValue() + ").");
        }
    }

    private void validatePassword(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        String password = value.toString();
        if (properties.required() && password.isEmpty()) {
            validationMessages.add(String.format(
                    "Es necesario rellenar el campo «%s».",
                    properties.label()));
            return;
        }

        if (!PASSWORD_PATTERN.matcher((CharSequence) password).matches()) {
            validationMessages.add(String.format(
                    "El campo «%s» no tiene la longitud adecuada.",
                    properties.label()));
        }
    }

    private void validateIpAddress(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        String ipAddress = value.toString();
        if (properties.required() && ipAddress.isEmpty()) {
            validationMessages.add(String.format(
                    "Es necesario rellenar el campo «%s».",
                    properties.label()));
            return;
        } else if (!properties.required() && Strings.isNullOrEmpty(
                ipAddress)) {
            // We don't have to validate this.
            return;
        }

        String[] ipAddressPieces = ipAddress.split("\\.");
        if (ipAddressPieces.length != 4) {
            validationMessages.add(String.format(
                    "El campo «%s» no es una dirección IP válida.",
                    properties.label()));
        } else {
            for (String ipPiece : ipAddressPieces) {
                Integer pieceNumber = Integer.parseInt(ipPiece);

                if (pieceNumber < 0 || pieceNumber > 255) {
                    validationMessages.add(String.format(
                            "El campo «%s» no es una dirección IP válida.",
                            properties.label()));
                    return;
                }
            }
        }
    }

    private void validateEmail(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        String email = value.toString();

        if (properties.required() && email.isEmpty()) {
            validationMessages.add(String.format(
                    "Es necesario rellenar el campo «%s».",
                    properties.label()));
            return;
        } else if (!properties.required() && Strings.isNullOrEmpty(
                email)) {
            // We don't have to validate this.
            return;
        }

        if (!getEmailValidator().validate(email)) {
            // The passed value isn't an email.
            validationMessages.add(
                    "El valor del campo «"
                    + properties.label()
                    + "» no es uno de los permitidos.");
        }
    }

    private void validateSelection(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        if (!value.getClass().isEnum()) {
            String selection = value.toString();
            if (properties.required() && selection.isEmpty()) {
                validationMessages.add(String.format(
                        "Es necesario rellenar el campo «%s».",
                        properties.label()));
                return;
            } else if (!properties.required() && Strings.isNullOrEmpty(
                    selection)) {
                // We don't have to validate this.
                return;
            }

            List<String> possibleValueList = Arrays.asList(
                    properties.possibleValues());

            // If external values is set, we cannot validate here
            if (!properties.externalValues() && !possibleValueList.contains(
                    selection)) {
                validationMessages.add(String.format(
                        "El valor del campo «%s» no es uno de los permitidos.",
                        properties.label()));
            }
        }
    }

    private void validateDecimal(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        String valStr = value + "";
        double decimal = Double.parseDouble(valStr);
        //Ensure that is formatted with a dot as decimal separator
        valStr = DecimalFormat.getNumberInstance(Locale.ENGLISH).format(decimal);

        if (properties.maxValue() < decimal) {
            validationMessages.add(
                    "El valor del campo «" + properties.label() + "» supera el máximo permitido (" + properties.maxValue() + ").");
        }

        if (properties.minValue() > decimal) {
            validationMessages.add(
                    "El valor del campo «" + properties.label() + "» es menor que el mímimo permitido (" + properties.minValue() + ").");
        }

        //Check decimal places
        int pos = valStr.indexOf(".");

        if (pos >= 0 && (valStr.length() - pos - 1) > properties.decimalPlaces()) {
            validationMessages.add(
                    "El valor del campo «" + properties.label() + "» debe tener " + properties.decimalPlaces() + " dígitos decimales como máximo.");
        }
    }

    private void validateAutocompletion(Object value, WidgetProps properties, List<String> validationMessages) {
        //This field works like a combobox for autocompletion but allow the
        //user to input anything they want so it also works as a textbox.
        //Then the validation applied is the one supported by the textbox
        validateText(value, properties, validationMessages);
    }

    private void validateFieldType(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        switch (properties.type()) {
            case CHECKBOX:
                break;
            case TEXT:
            case LONGTEXT:
                validateText(value, properties, validationMessages);
                break;
            case COLOR:
                validateColor(value, properties, validationMessages);
                break;
            case NUMBER:
                validateNumber(value, properties, validationMessages);
                break;
            case SELECTION:
                validateSelection(value, properties, validationMessages);
                break;
            case MULTISELECTION:
            case FILTER_MULTISELECT:
                validateMultiSelection(value, properties, validationMessages);
                break;
            case EMAIL:
                validateEmail(value, properties, validationMessages);
                break;
            case IPADDRESS:
                validateIpAddress(value, properties, validationMessages);
                break;
            case ROLESELECTOR:
                validateRoleSelector(value, properties, validationMessages);
                break;
            case PASSWORD:
            case PASSWORDEDITOR:
                // These two types are processed together as their only difference
                // is in the GUI.
                validatePassword(value, properties, validationMessages);
                break;
            case DECIMAL:
                validateDecimal(value, properties, validationMessages);
                break;
            case AUTOCOMPLETION:
                validateAutocompletion(value, properties, validationMessages);
                break;
            case FILE:
                validateFile(value, properties, validationMessages);
                break;
            case YEAR_DAY:
                validateYearDay(value, properties, validationMessages);
                break;
            case TIME:
                validateTime(value, properties, validationMessages);
                break;
            case DATE:
            case MONTH:
            case YEAR:
                validateDate(value, properties, validationMessages);
                break;
            case OLD_NIF:
                validateNIF(value, properties, validationMessages);
                break;
            case OLD_CIF:
                validateCIF(value, properties, validationMessages);
                break;
            case NIF:
                validateCIFOrNIF(value, properties, validationMessages);
                break;
        }
    }

    private void validateMultiSelectorHelper(Object value, WidgetProps properties,
            List<String> validationMessages, String[] possibleValues) {
        String[] selections = null;

        if (value.getClass() == String.class) {
            // We have the multi selection in a comma separated list.
            String valueString = value.toString();

            if (properties.required() && valueString.isEmpty()) {
                validationMessages.add(String.format(
                        "Es necesario rellenar el campo «%s».",
                        properties.label()));
                return;
            } else if (!properties.required() && Strings.isNullOrEmpty(
                    valueString)) {
                // We don't have to validate this.
                return;
            }

            selections = valueString.split(",");
        } else if (value instanceof String[]) {
            // We have a value list.
            selections = (String[]) value;
            if (selections.length == 0 && properties.required()) {
                validationMessages.add(String.format(
                        "Es necesario seleccionar una opción para el campo «%s».",
                        properties.label()));
                return;
            } else if (selections.length == 0 && !properties.required()) {
                return;
            }
        } else if (value instanceof List) {
            List list = (List) value;
            if (list.isEmpty() && properties.required()) {
                validationMessages.add(String.format(
                        "Es necesario seleccionar una opción para el campo «%s».",
                        properties.label()));
            }
            return;
        }

        if (!properties.externalValues() && selections != null) {
            List<String> possibleValueList = Arrays.asList(possibleValues);

            for (String selection : selections) {

                // If external values is set, we cannot validate here
                if (!possibleValueList.contains(selection)) {
                    validationMessages.add(String.format(
                            "Uno de los valores del campo «%s» no es uno de los permitidos.",
                            properties.label()));
                    break;
                }
            }

        }

    }

    private void validateRoleSelector(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        validateMultiSelectorHelper(
                value,
                properties,
                validationMessages,
                roleManager.getRoleNames());
    }

    private void validateFile(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        InputStream stream = (InputStream) value;

        if (properties.required() && value == null) {
            validationMessages.add(String.format(
                    "Es necesario seleccionar un archivo para el campo «%s».",
                    properties.label()));
            return;
        }

        if (properties.minSize() != 0 || properties.maxSize() != Integer.MAX_VALUE) {
            int size = 0;
            try {
                size = stream.available();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass()).error(ex);
                return;
            }
            boolean minSizeErr =
                    properties.minSize() > 0 && size < properties.minSize();
            boolean maxSizeErr =
                    properties.maxSize() < Integer.MAX_VALUE && size > properties.maxSize();

            if (minSizeErr && maxSizeErr) {
                validationMessages.add(String.format(
                        "El tamaño del archivo tiene que estar entre «%s» y «%s» bytes inclusives.",
                        properties.minSize(), properties.maxSize()));
            } else if (minSizeErr) {
                validationMessages.add(String.format(
                        "El tamaño del archivo tiene que ser superior a «%s» bytes.",
                        properties.minSize()));
            } else {
                validationMessages.add(String.format(
                        "El tamaño del archivo tiene que ser inferior a «%s» bytes.",
                        properties.maxSize()));
            }
        }


        //TODO: check file type
    }

    private void validateYearDay(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        if (value == null) {
            if (properties.required()) {
                validationMessages.add(String.format(
                        "Es necesario rellenar el campo «%s».",
                        properties.label()));
            }
            return;
        }

        int day = 0;
        try {
            day = (Integer) value;
        } catch (ClassCastException e) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» no es un número entero.",
                    properties.label()));
            return;
        }

        if (day < 1 || day > 365) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» debe estar entre 1 y 365",
                    properties.label()));
        }
    }

    private void validateTime(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {
        if (properties.required() && value != null) {

            validationMessages.add(String.format(
                    "El valor del campo «%s» es necesario.",
                    properties.label()));

        }

    }

    private void validateDate(
            Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        if (properties.required() && value == null) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» es necesario.",
                    properties.label()));
        }
    }

    private void validateNIF(Object value,
            WidgetProps properties,
            List<String> validationMessages) {

        if (properties.required() && value == null) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» es necesario.",
                    properties.label()));
            return;
        } else if (!properties.required() && value == null) {
            return;
        }

        NIFValidator validator = new NIFValidator();
        String nif = (String) value;
        // remove unwanted characters to left only numbers and letters
        nif = nif.replaceAll("[/b-]+", "");

        if (!validator.validate(nif)) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» no es un NIF válido.",
                    properties.label()));
        }
    }

    private void validateCIF(Object value, WidgetProps properties,
            List<String> validationMessages) {

        if (properties.required() && value == null) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» es necesario.",
                    properties.label()));
            return;
        } else if (!properties.required() && value == null) {
            return;
        }

        CIFValidator validator = new CIFValidator();
        String cif = (String) value;
        // remove unwanted characters to left only numbers and letters
        cif = cif.replaceAll("[/b-]+", "");

        if (!validator.validate(cif)) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» no es un NIF válido.",
                    properties.label()));
        }
    }

    private void validateCIFOrNIF(Object value, WidgetProps properties,
            List<String> validationMessages) {

        String cifOrNif = (String) value;
        if (!properties.required() && Strings.isNullOrEmpty(cifOrNif)) {
            return;
        }

        CIFOrNIFValidator validator = new CIFOrNIFValidator();

        // remove unwanted characters to left only numbers and letters
        cifOrNif = cifOrNif.replaceAll("[/b-]+", "");

        if (!validator.validate(cifOrNif)) {
            validationMessages.add(String.format(
                    "El valor del campo «%s» no es un NIF válido.",
                    properties.label()));
        }
    }

    /**
     * @return the emailValidator
     */
    public EmailValidator getEmailValidator() {

        if (emailValidator == null) {
            emailValidator = new EmailValidator();
        }
        return emailValidator;
    }
}
