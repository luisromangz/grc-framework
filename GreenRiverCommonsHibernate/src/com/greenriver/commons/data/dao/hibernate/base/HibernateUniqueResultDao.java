package com.greenriver.commons.data.dao.hibernate.base;

import com.greenriver.commons.data.DataEntity;
import java.lang.reflect.ParameterizedType;
import org.hibernate.Criteria;

/**
 * Base class for hibernate daos for entities of which there should be
 * only one instance in the database.
 * 
 * @author luis
 */
public class HibernateUniqueResultDao<T extends DataEntity>
        extends HibernateDaoBase<T> {

    public T get() {

        // We need the type used as template.
        ParameterizedType parameterizedType =
                (ParameterizedType) getClass().getGenericSuperclass();


        Criteria crit = getCurrentSession().createCriteria(
                (Class)parameterizedType.getActualTypeArguments()[0]);

        return (T) crit.uniqueResult();
    }

    public void save(T entity) {
        T existingEntity = get();
        if(existingEntity!=null) {
            // There can only be one!
            getCurrentSession().delete(existingEntity);
        }
        getCurrentSession().save(entity);

    }
}
