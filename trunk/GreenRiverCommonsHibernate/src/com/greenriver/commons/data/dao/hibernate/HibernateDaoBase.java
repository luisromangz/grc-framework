
package com.greenriver.commons.data.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author luis
 */
public abstract class HibernateDaoBase {
    private SessionFactory sessionFactory;

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
