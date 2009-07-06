
package com.greenriver.commons.data.transactions.test;

import com.greenriver.commons.data.transactions.TransactionManager;

/**
 *
 * @author luis
 */
public class TestTransactionManager implements TransactionManager{

    public void begin() {
        System.out.println("******** Transaction begin ***********");
    }

    public void commit() {
        System.out.println("******** Transaction commited ***********");
    }

    public void rollback() {
        System.out.println("******** Transaction rollbacked ***********");
    }

}
