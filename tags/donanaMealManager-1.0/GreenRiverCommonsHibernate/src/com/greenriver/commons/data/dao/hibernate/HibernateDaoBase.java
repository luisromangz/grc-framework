package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.data.dao.hibernate.pagination.CriteriaPagingHelperImpl;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import com.greenriver.commons.data.dao.hibernate.pagination.CriteriaPagingHelper;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

/**
 *
 * @author luis
 */
public abstract class HibernateDaoBase<T> {

    private Class entityClass = null;
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

    public <T> List<T> getFieldValues(String fieldName, Criterion... criterions) {

        Criteria crit = getCurrentSession().createCriteria(getEntityClass());
        crit.setProjection(Projections.groupProperty(fieldName).as("groupedName"));
        crit.addOrder(Order.asc("groupedName"));

        for(Criterion r : criterions) {
            crit.add(r);
        }

        return crit.list();
    }

    protected Class getEntityClass () {
        if(entityClass!=null) {
            return entityClass;
        }

        Type superclass = getClass().getGenericSuperclass();
        if(superclass == null
                || !(superclass instanceof ParameterizedType)) {
                throw new IllegalStateException("Entity template class needs to be specified when extending HibernateDaoBase.");
        }

        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        entityClass= (Class)parameterizedType.getActualTypeArguments()[0];
        return entityClass;
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
