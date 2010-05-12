package com.greenriver.commons;

import java.util.List;

/**
 *
 * @author luis
 */
public class ErrorMessagesException extends RuntimeException {
    private List<String> messages;
    public ErrorMessagesException(List<String> messages) {
        
    }

    /**
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }
}
