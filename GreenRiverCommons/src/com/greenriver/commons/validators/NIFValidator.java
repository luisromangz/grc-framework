
package com.greenriver.commons.validators;

import com.greenriver.commons.Strings;

/**
 * Validates an spanish NIF
 * @author Miguel Angel
 */
public class NIFValidator {
    /**
     * Ordered list of the 23 characters used as the validation code for NIF
     */
    public static String NIF_LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";
    // Maximum length of the NIF string plus letter, this allows 8 digit NIF
    // plus letter
    private static int MAX_LENGTH = 9;
    // Minimum length of the nif string plus letter (7 digit plus letter)
    private static int MIN_LENGTH = 8;

    /**
     * Validates a spanish NIF. The nif letter must be appended at the end of
     * the nif string for validation purposses and the string must only contain
     * numbers and that final letter.
     * @param nif
     * @return
     */
    public boolean validate(String nif) {

        if (Strings.isNullOrEmpty(nif)) {
            return false;
        }
        
        if(nif.length() < MIN_LENGTH || nif.length() > MAX_LENGTH) {
            return false;
        }
        
        Character ch = nif.charAt(nif.length() - 1);
        long numNif = 0L;

        nif = nif.substring(0, nif.length() - 1);

        try {
            numNif = Long.parseLong(nif);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return validate(numNif, ch);
    }

    public boolean validate(long nif, Character ch) {
        if (ch == null || !Character.isLetter(ch)) {
            return false;
        }
        ch = Character.toUpperCase(ch);
        return ch.equals(NIF_LETTERS.charAt((int)nif % 23));
    }

    /**
     * Gets the letter of the nif
     * @param nif Nif number as an string
     * @return the character or null if it fails
     */
    public Character getNifControlCharacter(String nif) {
        
        if (Strings.isNullOrEmpty(nif)) {
            return null;
        }

        // We only want to get the cif without the check char if specified
        Character ch = nif.charAt(nif.length() - 1);

        if (Character.isLetter(ch)) {
            nif = nif.substring(0, nif.length() - 1);
        }

        if(nif.length() < MIN_LENGTH - 1 || nif.length() > MAX_LENGTH - 1) {
            return null;
        }
        
        long numNif = 0L;

        try {
            numNif = Long.parseLong(nif);
        } catch (NumberFormatException nfe) {
            return null;
        }

        return getNifControlCharacter(numNif);
    }

    public Character getNifControlCharacter(long nif) {
        // It's as easy as calculating mod 23 to get a number in the range 0-22
        // and then get the letter from the letters string.
        return NIF_LETTERS.charAt((int)nif % 23);
    }
}
