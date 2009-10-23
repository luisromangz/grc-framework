
package com.greenriver.commons.data.dao.hibernate;

/**
 * @author Miguel Angel
 */
public class HibernateDaoException extends RuntimeException {

    public HibernateDaoException() {
        super();
    }

    public HibernateDaoException(String msg) {
        super(msg);
    }

    public HibernateDaoException(String msg, Throwable innerEx) {
        super(msg, innerEx);
    }
}
