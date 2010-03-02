
package com.greenriver.commons;

/**
 * Exception thrown when a field is tried to be found in a class but it didn't
 * existed.
 * @author Miguel Angel
 */
public class FieldNotFoundException extends RuntimeException {

    public FieldNotFoundException() {
        super();
    }

    public FieldNotFoundException(String msg) {
        super(msg);
    }
}
