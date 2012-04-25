package com.greenriver.commons.web.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class encapsulates the result given by a service call, so we can
 * return if the proccess was successful and an error message if it wasn't.
 * @param <T> The type of the contained payload.
 * @author luis
 */
public class Result<T> {

    private T result;
    private boolean success = true;
    private List<String> messages;
    
    private String status;
    
    public Result() {
        messages = new ArrayList<String>();
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
     * Gets the messages returned to the client by the service.
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Sets the messages returnet to the client by the service.
     * @param messages the messages to set
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
    
    /**
     * Add a message so its returnet to the client.
     * @param msg 
     */
    public void addMessage(String msg) {
        this.messages.add(msg);
    }
    
    /**
     * Formats a message and adds it, so its returned to the client.
     * @param format
     * @param elements 
     */
    public void formatMessage(String format, Object... elements) {        
        this.addMessage(String.format(format, elements));
    }

     
    public void addMessages(Collection<String> messages) {
        this.messages.addAll(messages);
    }
    
    /**
     * Sets messages to be returned to the client, and also sets the 
     * success flag to false.
     * @param errorMessages The error message to set
     */
    public void setErrorMessages(List<String> errorMessages) {
        this.success = false;
        this.setMessages(errorMessages);
    }
   
    
    /**
     * Adds collection of messages and sets the success flag to false.
     * @param errorMessages 
     */
    public void addErrorMessages(Collection<String> errorMessages) {
        this.success = false;
        this.addMessages(errorMessages);
    }
    
    /**
     * Formats an error mesage, and sets the success flag to false.
     * @param message
     * @param submessages 
     */
    public void formatErrorMessage(String message, Object... submessages) {
        this.success = false;
        this.formatMessage(message, submessages);
    }
    
    /**
     * Adds an error message, and sets the success flag to false.
     * @param message 
     */
    public void addErrorMessage(String message) {
        this.success = false;
        this.addMessage(message);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    
}
