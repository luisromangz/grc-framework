
package com.greenriver.commons.security;

import com.greenriver.commons.Strings;

/**
 * Validates a nif
 * @author Miguel Angel
 */
public class NIFValidator {
    // 23 letters used
    private static String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    // Maximum length of the nif string plus letter
    private static int MAX_LENGTH = 12;
    // Minimum length of the nif string plus letter
    private static int MIN_LENGTH = 6;

    /**
     * Validates a spanish NIF. The nif letter must be appended at the end of
     * the nif string for validation purposses and the string must only contain
     * numbers and that final letter.
     * @param nif
     * @return
     */
    public boolean validate(String nif) {

        if (Strings.isNullOrEmpty(nif) || nif.length() < MIN_LENGTH ||
                nif.length() > MAX_LENGTH) {
            return false;
        }
        
        Character ch = nif.charAt(nif.length() - 1);
        long numNif = 0L;

        if (!Character.isLetter(ch)) {
            return false;
        }

        nif = nif.substring(0, nif.length() - 1);

        try {
            numNif = Long.parseLong(nif);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return validate(numNif, ch);
    }

    public boolean validate(long nif, Character ch) {
        ch = Character.toUpperCase(ch);
        return ch.equals(LETTERS.charAt((int)nif % 23));
    }

    /**
     * Gets the letter of the nif
     * @param nif Nif number as an string
     * @return
     */
    public Character calculateNIFLetter(String nif) {
        if (Strings.isNullOrEmpty(nif) || nif.length() < MIN_LENGTH ||
                nif.length() > MAX_LENGTH) {
            return null;
        }

        Character ch = nif.charAt(nif.length() - 1);
        long numNif = 0L;

        if (Character.isLetter(ch)) {
            return null;
        }

        try {
            numNif = Long.parseLong(nif);
        } catch (NumberFormatException nfe) {
            return null;
        }

        return calculateNIFLetter(numNif);
    }

    public Character calculateNIFLetter(long nif) {
        // It's as easy as calculating mod 23 to get a number in the range 0-22
        // and then get the letter from the letters string.
        return LETTERS.charAt((int)nif % 23);
    }
}
