package com.greenriver.commons;

import java.util.List;

/**
 *
 * @author luis
 */
public class ErrorMessagesException extends Exception {
    private List<String> messages;
    public ErrorMessagesException(List<String> messages) {
        this.messages = messages;
    }

    /**
     * @return the messages
     */
    public List<String> getMessages() {
        return messages;
    }
}
