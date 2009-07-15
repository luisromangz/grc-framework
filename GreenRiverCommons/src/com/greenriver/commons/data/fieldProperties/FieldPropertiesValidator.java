package com.greenriver.commons.data.fieldProperties;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.validation.FieldsValidationResult;
import com.greenriver.commons.data.validation.FieldsValidator;
import com.greenriver.commons.roleManagement.RoleManager;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
    private RoleManager roleManager;

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
	    validateFieldType(value, properties, validationStrings);
	}

	return validationStrings;
    }

    public void setRoleManager(RoleManager roleManager) {
	this.roleManager = roleManager;
    }

    public RoleManager getRoleManager() {
	return this.roleManager;
    }

    private void validateMultiSelection(Object value,
	    FieldProperties properties,
	    List<String> validationStrings) {
	validateMultiSelectorHelper(value, properties, validationStrings,
		properties.possibleValues());

    }

    private void validateText(Object value, FieldProperties properties, List<String> validationStrings) {
	String text = (String) value;
	if (properties.required() && text.isEmpty()) {
	    validationStrings.add(String.format(
		    "El valor del campo «%s» es obligatorio.",
		    properties.label()));
	    return;
	} else if (!properties.required() && Strings.isNullOrEmpty(
		text)) {
	    // We don't have to validate this.
	    return;
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
    }

    private void validateColor(Object value, FieldProperties properties,
	    List<String> validationStrings) {
	String colorValue = (String) value;

	if (properties.required() && colorValue.isEmpty()) {
	    validationStrings.add(String.format(
		    "El valor del campo «%s» es obligatorio.",
		    properties.label()));
	    return;
	} else if (!properties.required() && Strings.isNullOrEmpty(
		colorValue)) {
	    // We don't have to validate this.
	    return;
	}

	CharSequence sequence = colorValue.subSequence(0,
		colorValue.length());

	if (!COLOR_PATTERN.matcher(sequence).matches()) {
	    validationStrings.add(
		    "El campo «" + properties.label() + "» no tiene formato de color RGB hexadecimal.");
	}
    }

    private void validateNumber(Object value, FieldProperties properties, List<String> validationStrings) {
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
    }

    private void validatePassword(Object value, FieldProperties properties, List<String> validationStrings) {
	String password = value.toString();
	if (properties.required() && password.isEmpty()) {
	    validationStrings.add(String.format(
		    "El valor del campo «%s» es obligatorio.",
		    properties.label()));
	    return;
	}


	CharSequence sequence1 = password.subSequence(0,
		password.length());
	if (!PASSWORD_PATTERN.matcher(sequence1).matches()) {
	    validationStrings.add(String.format(
		    "El campo «%s» no tiene la longitud adecuada.",
		    properties.label()));
	}
    }

    private void validateIpAddress(Object value, FieldProperties properties, List<String> validationStrings) {
	String ipAddress = value.toString();
	if (properties.required() && ipAddress.isEmpty()) {
	    validationStrings.add(String.format(
		    "El valor del campo «%s» es obligatorio.",
		    properties.label()));
	    return;
	} else if (!properties.required() && Strings.isNullOrEmpty(
		ipAddress)) {
	    // We don't have to validate this.
	    return;
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
		    return;
		}
	    }
	}
    }

    private void validateEmail(Object value, FieldProperties properties,
	    List<String> validationStrings) {
	String email = value.toString();
	if (properties.required() && email.isEmpty()) {
	    validationStrings.add(String.format(
		    "El valor del campo «%s» es obligatorio.",
		    properties.label()));
	    return;
	} else if (!properties.required() && Strings.isNullOrEmpty(
		email)) {
	    // We don't have to validate this.
	    return;
	}

	if (!EMAIL_PATTERN.matcher(email.subSequence(0,
		email.length() - 1)).matches()) {
	    // The passed value isn't an email.
	    validationStrings.add(
		    "El valor del campo «" + properties.label() + "» no es uno de los permitidos.");
	}
    }

    private void validateSelection(Object value, FieldProperties properties,
	    List<String> validationStrings) {
	if (!value.getClass().isEnum()) {
	    String selection = value.toString();
	    if (properties.required() && selection.isEmpty()) {
		validationStrings.add(String.format(
			"El valor del campo «%s» es obligatorio.",
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
		validationStrings.add(String.format(
			"El valor del campo «%s» no es uno de los permitidos.",
			properties.label()));
	    }
	}
    }

    private void validateDecimal(Object value, FieldProperties properties, List<String> validationStrings) {
	String valStr = value + "";
	double decimal = Double.parseDouble(valStr);
	//Ensure that is formatted with a dot as decimal separator
	valStr = DecimalFormat.getNumberInstance(Locale.ENGLISH).format(decimal);

	if (properties.maxValue() < decimal) {
	    validationStrings.add(
		    "El valor del campo «" + properties.label() + "» supera el máximo permitido (" + properties.maxValue() + ").");
	}

	if (properties.minValue() > decimal) {
	    validationStrings.add(
		    "El valor del campo «" + properties.label() + "» es menor que el mímimo permitido (" + properties.minValue() + ").");
	}

	//Check decimal places
	int pos = valStr.indexOf(".");

	if (pos >= 0 && (valStr.length() - pos - 1) > properties.decimalPlaces()) {
	    validationStrings.add(
		    "El valor del campo «" + properties.label() + "» debe tener " + properties.decimalPlaces() + " dígitos decimales como máximo.");
	}
    }

    private void validateAutocompletion(Object value, FieldProperties properties, List<String> validationStrings) {
	//This field works like a combobox for autocompletion but allow the
	//user to input anything they want so it also works as a textbox.
	//Then the validation applied is the one supported by the textbox
	validateText(value, properties, validationStrings);
    }

    private void validateFieldType(Object value, FieldProperties properties, List<String> validationStrings) {
	switch (properties.type()) {
	    case BOOLEAN:
		break;
	    case TEXT:
	    case LONGTEXT:
		validateText(value, properties, validationStrings);
		break;
	    case COLOR:
		validateColor(value, properties, validationStrings);
		break;
	    case NUMBER:
		validateNumber(value, properties, validationStrings);
		break;
	    case SELECTION:
		validateSelection(value, properties, validationStrings);
		break;
	    case MULTISELECTION:
		validateMultiSelection(value, properties, validationStrings);
		break;
	    case EMAIL:
		validateEmail(value, properties, validationStrings);
		break;
	    case IPADDRESS:
		validateIpAddress(value, properties, validationStrings);
		break;
	    case ROLESELECTOR:
		validateRoleSelector(value, properties, validationStrings);
		break;
	    case PASSWORD:
	    case PASSWORDEDITOR:
		// These two types are processed together as their only difference
		// is in the GUI.
		validatePassword(value, properties, validationStrings);
		break;
	    case DECIMAL:
		validateDecimal(value, properties, validationStrings);
		break;
	    case AUTOCOMPLETION:
		validateAutocompletion(value, properties, validationStrings);
		break;
	}
    }

    private void validateMultiSelectorHelper(Object value, FieldProperties properties,
	    List<String> validationStrings, String[] possibleValues) {
	String[] selections = null;

	if (value.getClass() == String.class) {
	    // We have the multi selection in a comma separated list.
	    String valueString = value.toString();

	    if (properties.required() && valueString.isEmpty()) {
		validationStrings.add(String.format(
			"El valor del campo «%s» es obligatorio.",
			properties.label()));
		return;
	    } else if (!properties.required() && Strings.isNullOrEmpty(
		    valueString)) {
		// We don't have to validate this.
		return;
	    }

	    selections = valueString.split(",");
	} else {
	    // We have a value list.
	    selections = (String[]) value;
	    if (selections.length == 0 && properties.required()) {
		validationStrings.add(String.format(
			"El valor del campo «%s» es obligatorio.",
			properties.label()));
		return;
	    } else if (selections.length == 0 && !properties.required()) {
		return;
	    }
	}

	List<String> possibleValueList = Arrays.asList(possibleValues);

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
    }

    private void validateRoleSelector(Object value, FieldProperties properties,
	    List<String> validationStrings) {
	validateMultiSelectorHelper(value, properties, validationStrings,
		roleManager.getRoleNames());
    }
}
