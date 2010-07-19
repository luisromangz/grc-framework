package com.greenriver.commons.services;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the result given by a service call, so we can
 * return if the proccess was successful and an error message if it wasn't.
 * @param <T> The type of the contained payload.
 * @author luis
 */
public class ServiceResult<T> {
    private T result;
    private boolean success = true;
    private List<String> errorMessages;

    public ServiceResult() {
        errorMessages = new ArrayList<String>();
    }

    /**
     * Gets the useful object returned by the service.
     * @return The service's result.
     */
    public T getResult() {
        return result;
    }

    /**
     * Sets the useful object returned by the service.
     * @param result The result to be set.
     */
    public void setResult(T result) {
        this.result = result;
    }

    /**
     * Tells if the service execution was successful or not.
     * @return The success status.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the sucess status for the service's call.
     * @param success The success status to set.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Gets the error message explaining why the service call failed.
     * @return The errorMessage produced by the service.
     */
    public List<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Sets an error message explaining why the service call failed.
     * @param errorMessages The error message to set
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessages(List<String> errorMessages) {
        this.errorMessages.addAll(errorMessages);
    }
    
    public void formatErrorMessage(String message, Object ... submessages) {
       this.errorMessages.add(String.format(message, submessages));
    }

    public void addErrorMessage(String message) {
       this.errorMessages.add(message);
    }

    
}
