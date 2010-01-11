
package com.greenriver.commons.security;

import com.greenriver.commons.Strings;

/**
 * @author Miguel Angel
 */
public class CIFOrNIFValidator {
    private static int MAX_LENGTH = 9;
    private static int MIN_LENGTH = 8;

    /**
     * @param cifOrNif
     * @return
     */
    public boolean validate(String cifOrNif) {

        if (Strings.isNullOrEmpty(cifOrNif)) {
            return false;
        }

        if(cifOrNif.length() < MIN_LENGTH || cifOrNif.length() > MAX_LENGTH) {
            return false;
        }

        Character ch = cifOrNif.charAt(cifOrNif.length() - 1);
        long numNif = 0L;

        if (!Character.isLetter(ch)) {
            return false;
        }

        ch = cifOrNif.charAt(0);

        if (!Character.isLetter(ch)) {
            return false;
        }

        cifOrNif = cifOrNif.substring(1, cifOrNif.length() - 1);

        try {
            numNif = Long.parseLong(cifOrNif);
        } catch (NumberFormatException nfe) {
            return false;
        }

        // TODO: Finish the validation, until that return true
        return true;
    }
}
