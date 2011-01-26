package com.greenriver.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This exception is used to carry multiple error messages instead of just one.
 * @author luis
 */
public class ErrorMessagesException extends Exception {
    private List<String> messages = new ArrayList<String>();

    public ErrorMessagesException(String message) {
        messages.add(message);
    }

    public ErrorMessagesException(List<String> messages) {
        this.messages = messages;
    }

    public ErrorMessagesException() {
    }

    /**
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }

    public void addMessage(String message) {
        if(!messages.contains(message)) {
            messages.add(message);
        }
        
    }

    public void addMessages(Collection<String> messages) {
        for(String message : messages) {
            addMessage(message);
        }
    }

    public boolean hasErrors() {
        return !messages.isEmpty();
    }
}
