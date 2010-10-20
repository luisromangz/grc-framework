package com.greenriver.commons.data.validation;

/**
 * This interface sets the contract for a class which will use a FieldsValidator
 * instance.
 * @author luis
 */
public interface FieldsValidatorClient {
    /**
     * Gets the FieldsValidator object asigned.
     * @return The fields validator.
     */
    FieldsValidator getFieldsValidator();

    /**
     * Sets the FieldsValidator object.
     * @param fieldsValidator The FieldsValidator object to be used.
     */
    void setFieldsValidator(FieldsValidator fieldsValidator);
}
