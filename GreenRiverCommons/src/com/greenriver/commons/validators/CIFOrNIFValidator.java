
package com.greenriver.commons.validators;

import com.greenriver.commons.Strings;

/**
 * Validator for those
 * @author Miguel Angel
 */
public class CIFOrNIFValidator implements DataValidator<String> {

    /**
     * @param cifOrNif
     * @return
     */
    @Override
    public boolean validate(String cifOrNif) {

        if (Strings.isNullOrEmpty(cifOrNif)) {
            return false;
        }

        NIFValidator nifValidator = new NIFValidator();
        if (nifValidator.validate(cifOrNif)) {
            return true;
        }

        CIFValidator cifValidator = new CIFValidator();
        if (cifValidator.validate(cifOrNif)) {
            return true;
        }

        return false;
    }
}
