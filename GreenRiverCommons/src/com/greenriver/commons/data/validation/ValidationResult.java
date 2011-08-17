
package com.greenriver.commons.data.validation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author luis
 */
public class ValidationResult {
    private boolean valid;
    private List<String> errorMessages;

    public ValidationResult() {
        errorMessages = new ArrayList<String>();
        valid = true;
    }

    public void addErrorMessages(List<String> fieldValidation) {
        errorMessages.addAll(fieldValidation);
        valid = false;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the errorMessages
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessages the errorMessages to set
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessage(String errorMessage) {
        errorMessages.add(errorMessage);
        this.valid = false;
    }
}
