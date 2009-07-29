package com.greenriver.commons;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods to handle enums.
 * @author Miguel Angel
 */
public class Enums {

    /**
     * Gets a map between enum constant names and enum constant labels. It is
     * required that a method is defined for each enum constant to return the
     * label. The signature of that method must be <b>String methodName()</b>.
     * @param enumClass Type of the enumeration
     * @param mapMethodName Method name of the enum constants that returns the label.
     * @return a map between enum constant names and labels
     */
    public static Map<String, String> getMapping(Class enumClass,
	    String mapMethodName) {
	
	HashMap<String, String> mapping = new HashMap<String, String>();
	Class enumConstClass = null;
	Method method = null;
	String label = null;
	String name = null;

	if (!enumClass.isEnum()) {
	    throw new IllegalArgumentException(
		    "The class is not an enumeration type");
	}

	try {

	    for (Object obj : enumClass.getEnumConstants()) {
		enumConstClass = obj.getClass();
		//We use reflection to get methods from the enum constant and
		//invoke them to return the string
		method = enumConstClass.getMethod(mapMethodName);
		label = (String) method.invoke(obj);

		if (Strings.isNullOrEmpty(label)) {
		    throw new IllegalArgumentException("Empty label found");
		}

		method = enumConstClass.getMethod("name");
		name = (String) method.invoke(obj);

		mapping.put(name, label);
	    }

	} catch (NoSuchMethodException ex) {
	    throw new IllegalArgumentException("The method " + mapMethodName +
		    " is not defined");
	} catch (IllegalAccessException ex) {
	    throw new IllegalStateException("Can't invoke", ex);
	} catch (InvocationTargetException ex) {
	    throw new IllegalStateException("Can't invoke", ex);
	} catch (ClassCastException ex) {
	    throw new IllegalStateException(
		    "The method doesn't returns an string");
	}

	return mapping;
    }
}
