package com.greenriver.commons.data.transactions.hibernate;

import com.greenriver.commons.data.transactions.TransactionManager;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TransactionException;

/**
 * This class implements an Hibernate transaction manager prepared to be
 * used in an aspect-fashioned way or programatically.
 *
 * If you choose to use it as an aspect, then begin must be called before
 * any dao method is called, and rollback has to be called when a dao method
 * returns because it has launched an exception.
 *
 * The commint method must be called by the aspect after the method calling all
 * dao objects has returned without problems.
 * @author luis
 */
public class HibernateTransactionManager implements TransactionManager {

    public void begin() {
        Transaction tran = sessionFactory.getCurrentSession().getTransaction();

        if (!tran.isActive()) {
            sessionFactory.getCurrentSession().beginTransaction();
        }
    }

    public void commit() {
        Transaction tran = sessionFactory.getCurrentSession().getTransaction();

        if (tran.isActive()) {
            sessionFactory.getCurrentSession().getTransaction().commit();
        }
    }

    public void rollback() {
        Transaction tran = sessionFactory.getCurrentSession().getTransaction();

        if (tran.isActive()) {
            try{
                sessionFactory.getCurrentSession().getTransaction().rollback();
            } catch (TransactionException tE){
                // Do nothing;
            }
        }
    }
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
