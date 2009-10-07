
package com.greenriver.commons.data.validation;

/**
 * @author Miguel Angel
 */
public class ValidationRegex {
    public static String EMAIL = "[\\w\\d._%+-]+@[\\w\\d.-]+\\.[\\w]{2,6}";
    public static String COLOR = "#[0-9[A-F]]{6}";
    public static String PASSWORD_ALPHA_6 ="\\w{6,}";
}
