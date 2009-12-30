
package com.greenriver.commons.data;

/**
 * @author Miguel Angel
 */
public class DataCorruptionException extends RuntimeException {

    public DataCorruptionException() {
        super();
    }

    public DataCorruptionException(String msg) {
        super(msg);
    }

    public DataCorruptionException(String msg, Exception inner) {
        super(msg, inner);
    }
}
