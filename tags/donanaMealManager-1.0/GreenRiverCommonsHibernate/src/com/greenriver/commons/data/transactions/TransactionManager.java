package com.greenriver.commons.data.transactions;

/**
 *
 * @author luis
 */
public interface TransactionManager {
    public void begin();
    public void commit();
    public void rollback();
}
