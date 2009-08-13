package com.greenriver.commons;

/**
 * Utitlities to work with Class instances
 * @author Miguel Angel
 */
public class Classes {

    /**
     * Gets if a field is defined in the class
     * @param fieldName Name of the field (case-sensitive)
     * @param cls Class object to search the field for.
     * @return
     */
    public static boolean hasDeclaredField(String fieldName, Class cls) {
        boolean result = false;

        try {
            return cls.getDeclaredField(fieldName) != null;
        } catch (NoSuchFieldException ex) {
        } catch (SecurityException ex) {
        }

        return result;
    }
}
