
package com.greenriver.commons.validators;

import com.greenriver.commons.Strings;

/**
 * Validator for those ids given to legal entities. Since 2008 these ids are also
 * known as NIFs but CIF was the previous name given to them. The current validation
 * works only for legal entities and not for private individuals.
 * @author Miguel Angel
 */
public class CIFValidator implements DataValidator<String> {
    // CIF chars
    public static String CIF_CHARS = "ABCDEFGHJKLMNPQRSUVW";
    public static String CHAR_CHECK = "OABCDEFGHIJ";
    // CIF letters that need a numeric validation
    public static String NUMERIC_ORGS = "ABDEFGHJUV";

    // Maximum length of the NIF string plus letter, this allows 8 digit NIF
    // plus letter
    private static int MAX_LENGTH = 9;
    // Minimum length of the nif string plus letter (7 digit plus letter)
    private static int MIN_LENGTH = 9;
    
    @Override
    public boolean validate(String cif) {

        if (Strings.isNullOrEmpty(cif)) {
            return false;
        }

        if(cif.length() < MIN_LENGTH || cif.length() > MAX_LENGTH) {
            return false;
        }

        Character checkChar = cif.charAt(cif.length() - 1);

        cif = cif.substring(0, cif.length() - 1);

        return validate(cif, checkChar);
    }

    public boolean validate(Character orgChar, long cif, Character controlChar) {
        orgChar = Character.toUpperCase(orgChar);
        controlChar = Character.toUpperCase(controlChar);
        return validate(orgChar.toString() + cif, controlChar);
    }

    public boolean validate(String cif, Character checkChar) {

        if(cif.length() < MIN_LENGTH - 1 || cif.length() > MAX_LENGTH ||
                checkChar == null ||
                (!Character.isDigit(checkChar) && !Character.isLetter(checkChar))) {
            return false;
        }

        checkChar = Character.toUpperCase(checkChar);
        Character calculatedCheckChar = this.getCifControlCharacter(cif);
        return calculatedCheckChar != null && calculatedCheckChar.equals(checkChar);
    }

    public Character getCifControlCharacter(String cif) {

        if(cif.length() < MIN_LENGTH - 1 || cif.length() > MAX_LENGTH) {
            return null;
        }

        Character orgChar = cif.charAt(0);

        if (orgChar == null || !Character.isLetter(orgChar)) {
            return null;
        }

        if (!Character.isDigit(cif.charAt(cif.length() - 1))) {
            cif = cif.substring(1, cif.length() - 1);
        } else {
            cif = cif.substring(1, cif.length());
        }

        if (!Strings.isInteger(cif)) {
            return null;
        }

        int a = 0;
        int b = 0;
        int c = 0;
        boolean isNumericCheckChar = NUMERIC_ORGS.indexOf(orgChar.toString()) >= 0;

        // calculate a and b

        for (int i = 0; i< cif.length(); i++) {
            int digit = (int)Integer.parseInt(cif.charAt(i) + "");

            if (i%2 != 0) {
                a += digit;
            } else {
                b += Strings.sumDigits("0" + (2 * digit));
            }
        }

        // calculate c

        String cStr = "" + (a + b);
        c = 10 - (int)Integer.parseInt(cStr.charAt(cStr.length() - 1) + "");

        // Calculate the control char of the cif

        if (!isNumericCheckChar) {
            // Not numeric check, is a char in CHAR_CHECK constant
            return (Character) CHAR_CHECK.charAt(c);
        } else {
            // Numeric
            cStr = "" + c;
            return (Character) cStr.charAt(cStr.length()-1);
        }
    }
}
