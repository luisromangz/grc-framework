package com.greenriver.commons.data.dao.hibernate.base;

import com.greenriver.commons.data.DataEntity;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Base class for Hibernate-based daos returning multiple results.
 * @author luis
 */
public class HibernateMultipleResultsDao<T extends DataEntity>
        extends HibernateDaoBase<T> {    
    
    public T get(T entity) {
        return (T) getCurrentSession().get(this.getEntityClass(), entity.getId());
    }

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
    
    public void remove(T element) {
        getCurrentSession().delete(element);
    }
}
