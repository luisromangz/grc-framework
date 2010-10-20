package com.greenriver.commons.data.dao.hibernate;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 *
 * @author luis
 */
public class HibernateMultipleResultsDao<T>
        extends HibernateDaoBase<T> {    

    public T getById(Long entityId) {

        return (T) getCurrentSession().get(this.getEntityClass(), entityId);
        
    }

    public List<T> getAll(Order order, Criterion... criterions) {
        Criteria crit = getCurrentSession().createCriteria(getEntityClass());

        crit.addOrder(order);

        for(Criterion c : criterions) {
            crit.add(c);
        }

        return crit.list();
    }    

    public void save(T entity) {
        
        getCurrentSession().saveOrUpdate(entity);
        getCurrentSession().flush();

    }

    public void remove(Long id) {
        // We implement hard removal.
        getCurrentSession().delete(this.getById(id));
    }
}
