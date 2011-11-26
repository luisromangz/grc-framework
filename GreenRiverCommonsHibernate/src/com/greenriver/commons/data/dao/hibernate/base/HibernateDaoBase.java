package com.greenriver.commons.data.dao.hibernate.base;

import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
public abstract class HibernateDaoBase<T extends DataEntity> {

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
    
    protected Criteria createCriteria(Class entityClass) {
        return getCurrentSession().createCriteria(entityClass);
    }

    public <T> List<T> getFieldValues(String fieldName, Criterion... criterions) {

        Criteria crit = getCurrentSession().createCriteria(getEntityClass());
        crit.setProjection(Projections.groupProperty(fieldName).as("groupedName"));
        crit.addOrder(Order.asc("groupedName"));

        for (Criterion r : criterions) {
            crit.add(r);
        }

        return crit.list();
    }

    protected Class getEntityClass() {
        if (entityClass != null) {
            return entityClass;
        }

        entityClass = getArgumentClass(DataEntity.class);
        return entityClass;
    }

    protected Class getArgumentClass(Class baseClass) {

        Class type = this.getClass();

        List<Class> argumentClasses = new ArrayList<Class>();

        while (type.getGenericSuperclass() != Object.class) {
            Type superclass = type.getGenericSuperclass();

            if (superclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) superclass;

                List<Class> localArguments = new ArrayList<Class>();
                for (Type t : parameterizedType.getActualTypeArguments()) {
                    if (t instanceof Class) {
                        Class c = (Class) t;
                        if (baseClass.isAssignableFrom(c)) {
                            localArguments.add(c);
                        }
                    }

                }

                argumentClasses.addAll(0, localArguments);
            }



            type = type.getSuperclass();
        }

        if (argumentClasses.size() > 1) {
            throw new IllegalStateException(String.format("Class '%s' has more than two template parameters of type '%s'.",
                    this.getClass().getName(),
                    baseClass.getName()));
        }

        return argumentClasses.get(0);
    }

    // <editor-fold defaultstate="collapsed" desc="Criteria factory related methods">
    protected CriteriaFactory getCriteriaFactory() {
        // For now is easier to instance this here
        return new CriteriaFactoryImpl(this.sessionFactory.getCurrentSession());
    }

    protected Criteria createCriteria(QueryArgs entityQueryArguments, Criterion... restrictions) {
        return getCriteriaFactory().createCriteria(
                getEntityClass(), entityQueryArguments, restrictions);
    }

    protected Criteria createPagedCriteria(QueryArgs queryArgs, Criterion... restrictions) {
        return getCriteriaFactory().createPagedCriteria(
                getEntityClass(), queryArgs, restrictions);
    }

    protected Criteria createCountingCriteria(QueryArgs queryArgs, Criterion... restrictions) {
        return getCriteriaFactory().createCountingCriteria(
                getEntityClass(), queryArgs, restrictions);
    }
    // </editor-fold>
}
