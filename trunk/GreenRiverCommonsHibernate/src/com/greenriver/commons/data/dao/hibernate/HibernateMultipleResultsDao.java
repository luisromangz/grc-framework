package com.greenriver.commons.data.dao.hibernate;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 *
 * @author luis
 */
public class HibernateMultipleResultsDao<T>
        extends HibernateDaoBase {

    private Class entityClass = null;

    public T getById(Long entityId) {

        return (T) getCurrentSession().get(this.getEntityClass(), entityId);
        
    }

    public List<T> getAll(String sortingField, boolean descending) {
        Criteria crit = getCurrentSession().createCriteria(getEntityClass());

        crit.addOrder(descending?Order.desc(sortingField):Order.asc(sortingField));

        return crit.list();
    }

    private Class getEntityClass () {
        if(entityClass!=null) {
            return entityClass;
        }
        ParameterizedType parameterizedType =
                (ParameterizedType) getClass().getGenericSuperclass();
        entityClass= (Class)parameterizedType.getActualTypeArguments()[0];
        return entityClass;
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
