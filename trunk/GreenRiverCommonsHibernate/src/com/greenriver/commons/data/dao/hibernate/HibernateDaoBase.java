package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.data.dao.hibernate.pagination.CriteriaPagingHelperImpl;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import com.greenriver.commons.data.dao.hibernate.pagination.CriteriaPagingHelper;
import org.hibernate.Criteria;
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

    // <editor-fold defaultstate="collapsed" desc="Criteria factory related methods">
    protected CriteriaFactory getCriteriaFactory() {
        // For now is easier to instance this here
        return new CriteriaFactoryImpl(this.sessionFactory.getCurrentSession());
    }

    protected Criteria createCriteriaFromQueryArguments(EntityQueryArguments entityQueryArguments) {
        return getCriteriaFactory().createCriteriaFromQueryArguments(entityQueryArguments);
    }

    protected Criteria createPaginatedCriteriaFromQueryArguments(int page, int pageSize, EntityQueryArguments entityQueryArguments) {
        return getCriteriaFactory().createPaginatedCriteriaFromQueryArguments(page, pageSize, entityQueryArguments);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Pagination using a helper">
    protected CriteriaPagingHelper getPagingHelper(EntityQueryArguments args) {
        CriteriaFactory critFactory = this.getCriteriaFactory();
        // For now is easier to instance this here
        CriteriaPagingHelper helper = new CriteriaPagingHelperImpl(critFactory, args);
        return helper;
    }
    // </editor-fold>
}
