package com.greenriver.commons.data.fieldProperties;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author luis
 */
public class FieldPropertiesValidator implements FieldsValidator {

    public static final Pattern COLOR_PATTERN = Pattern.compile(
            "^#[0-9[A-F]]{6}$",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^\\w{6,}$");

    public FieldsValidationResult validate(Object object) {
        FieldsValidationResult result = new FieldsValidationResult();

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            FieldProperties properties = field.getAnnotation(
                    FieldProperties.class);
            if (properties != null) {
                // The field has FieldProperties annotation, so it can be validated.               

                List<String> fieldErrorMessages = this.validateField(field,
                        properties, object);
                if (!fieldErrorMessages.isEmpty()) {
                    result.addErrorMessages(fieldErrorMessages);
                }
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<String> validateField(Field field, FieldProperties properties, Object object) {
        List<String> validationStrings = new ArrayList<String>();

        if (!(properties.visible() && properties.editable())) {
            // There wasn't user input, so there is nothing to validate.
            return validationStrings;
        }

        // We compose the name of the accesor;
        String methodName = field.getName().substring(0,
                1).toUpperCase() + field.getName().substring(1);

        if (field.getType() == boolean.class) {
            methodName = "is" + methodName;
        } else {
            methodName = "get" + methodName;
        }

        Object value = null;
        try {

            value = object.getClass().getMethod(methodName).invoke(object);

        } catch (Exception ex) {
            validationStrings.add(
                    "Ocurrió una excepción al recuperar el valor de un campo: " + ex.getLocalizedMessage());
            return validationStrings;
        }

        if (properties.required() && value == null) {
            validationStrings.add(String.format(
                    "El valor del campo «%s» es obligatorio.",
                    properties.label()));
        } else if (value != null) {

            switch (properties.type()) {
                case BOOLEAN:
                    break;
                case TEXT:
                case LONGTEXT:
                    String text = (String) value;
                    if (properties.required() && text.isEmpty()) {
                        validationStrings.add(String.format(
                                "El valor del campo «%s» es obligatorio.",
                                properties.label()));
                        break;
                    } else if(!properties.required()
                            && Strings.isNullOrEmpty(text)) {
                        // We don't have to validate this.
                        break;
                    }

                    if (!properties.customRegExp().isEmpty()) {
                        Pattern textPattern = Pattern.compile(
                                properties.customRegExp());
                        CharSequence seq =
                                text.subSequence(0, text.length());
                        if (!textPattern.matcher(seq).matches()) {
                            validationStrings.add(
                                    "El campo «" + properties.label() + "» no tiene un formato válido.");
                        }
                    }

                    break;
                case COLOR:

                    String colorValue = (String) value;

                    if (properties.required() && colorValue.isEmpty()) {
                        validationStrings.add(String.format(
                                "El valor del campo «%s» es obligatorio.",
                                properties.label()));
                        break;
                    } else if (!properties.required() && Strings.isNullOrEmpty(
                            colorValue)) {
                        // We don't have to validate this.
                        break;
                    }

                    CharSequence sequence = colorValue.subSequence(0,
                            colorValue.length());

                    if (!COLOR_PATTERN.matcher(sequence).matches()) {
                        validationStrings.add(
                                "El campo «" + properties.label() + "» no tiene formato de color RGB hexadecimal.");
                    }
                    break;

                case NUMBER:
                    // We parse the number so we don't need to deal with
                    // diferent integer types.
                    Integer number = Integer.parseInt(value.toString());


                    if (properties.maxValue() < number) {
                        validationStrings.add(
                                "El valor del campo «" + properties.label() + "» supera el máximo permitido (" + properties.maxValue() + ").");
                    }

                    if (properties.minValue() > number) {
                        validationStrings.add(
                                "El valor del campo «" + properties.label() + "» es menor que el mímimo permitido (" + properties.minValue() + ").");
                    }

                    break;

                case SELECTION:

                    if (!value.getClass().isEnum()) {
                        String selection = value.toString();
                        if (properties.required() && selection.isEmpty()) {
                            validationStrings.add(String.format(
                                    "El valor del campo «%s» es obligatorio.",
                                    properties.label()));
                            break;
                        } else if (!properties.required() && Strings.isNullOrEmpty(
                                selection)) {
                            // We don't have to validate this.
                            break;
                        }

                        List<String> possibleValueList = Arrays.asList(
                                properties.possibleValues());

                        // If external values is set, we cannot validate here
                        if (!properties.externalValues() && !possibleValueList.contains(
                                selection)) {
                            validationStrings.add(String.format(
                                    "El valor del campo «%s» no es uno de los permitidos.",
                                    properties.label()));
                        }
                    }

                    break;

                case MULTISELECTION:

                    String[] selections = null;

                    if (value.getClass() == String.class) {
                        // We have the multi selection in a comma separated list.
                        String valueString = value.toString();

                        if (properties.required() && valueString.isEmpty()) {
                            validationStrings.add(String.format(
                                    "El valor del campo «%s» es obligatorio.",
                                    properties.label()));
                            break;
                        } else if (!properties.required() && Strings.isNullOrEmpty(
                                valueString)) {
                            // We don't have to validate this.
                            break;
                        }


                        selections = valueString.split(",");


                    } else {
                        // We have a value list.
                        selections = (String[]) value;
                        if (selections.length == 0 && properties.required()) {
                            validationStrings.add(String.format(
                                    "El valor del campo «%s» es obligatorio.",
                                    properties.label()));
                            break;
                        } else if (selections.length == 0 && !properties.required()) {
                            break;
                        }
                    }

                    List<String> possibleValueList = Arrays.asList(
                            properties.possibleValues());

                    if (selections != null) {
                        for (String selection : selections) {

                            // If external values is set, we cannot validate here
                            if (!properties.externalValues() && !possibleValueList.contains(
                                    selection)) {
                                validationStrings.add(String.format(
                                        "Uno de los valores del campo «%s» no es uno de los permitidos.",
                                        properties.label()));
                                break;
                            }
                        }
                    }

                    break;

                case EMAIL:
                    String email = value.toString();
                    if (properties.required() && email.isEmpty()) {
                        validationStrings.add(String.format(
                                "El valor del campo «%s» es obligatorio.",
                                properties.label()));
                        break;
                    } else if (!properties.required() && Strings.isNullOrEmpty(
                            email)) {
                        // We don't have to validate this.
                        break;
                    }

                    if (!EMAIL_PATTERN.matcher(email.subSequence(0,
                            email.length() - 1)).matches()) {
                        // The passed value isn't an email.
                        validationStrings.add(
                                "El valor del campo «" + properties.label() + "» no es uno de los permitidos.");
                    }

                    break;

                case IPADDRESS:
                    String ipAddress = value.toString();
                    if (properties.required() && ipAddress.isEmpty()) {
                        validationStrings.add(String.format(
                                "El valor del campo «%s» es obligatorio.",
                                properties.label()));
                        break;
                    } else if (!properties.required() && Strings.isNullOrEmpty(
                            ipAddress)) {
                        // We don't have to validate this.
                        break;
                    }

                    String[] ipAddressPieces = ipAddress.split("\\.");
                    if (ipAddressPieces.length != 4) {
                        validationStrings.add(String.format(
                                "El campo «%s» no es una dirección IP válida.",
                                properties.label()));
                    } else {
                        for (String ipPiece : ipAddressPieces) {
                            Integer pieceNumber = Integer.parseInt(ipPiece);

                            if (pieceNumber < 0 || pieceNumber > 255) {
                                validationStrings.add(String.format(
                                        "El campo «%s» no es una dirección IP válida.",
                                        properties.label()));
                                break;
                            }
                        }
                    }

                    break;

                case PASSWORD:
                case PASSWORDEDITOR:
                    // These two types are processed together as their only difference
                    // is in the GUI.
                    String password = value.toString();
                    if (properties.required() && password.isEmpty()) {
                        validationStrings.add(String.format(
                                "El valor del campo «%s» es obligatorio.",
                                properties.label()));
                        break;
                    }


                    CharSequence sequence1 = password.subSequence(0,
                            password.length());
                    if (!PASSWORD_PATTERN.matcher(sequence1).matches()) {
                        validationStrings.add(String.format(
                                "El campo «%s» no tiene la longitud adecuada.",
                                properties.label()));
                    }

                    break;
            }
        }



        return validationStrings;
    }
}
