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

    @Override
    public void begin() {
        Transaction tran = sessionFactory.getCurrentSession().getTransaction();

        if (!tran.isActive()) {
            sessionFactory.getCurrentSession().beginTransaction();
        }
    }

    @Override
    public void commit() {
        Transaction tran = sessionFactory.getCurrentSession().getTransaction();

        if (tran.isActive()) {
            sessionFactory.getCurrentSession().flush();
            sessionFactory.getCurrentSession().getTransaction().commit();
        }
        
        sessionFactory.getCurrentSession().close();
    }

    @Override
    public void rollback() {
        Transaction tran = sessionFactory.getCurrentSession().getTransaction();

        sessionFactory.getCurrentSession().clear();
        if (tran.isActive()) {
            try{
                tran.rollback();
            } catch (TransactionException tE){
                // Do nothing;
                tE.printStackTrace(System.out);
            }
        }        

        sessionFactory.getCurrentSession().close();
    }
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
