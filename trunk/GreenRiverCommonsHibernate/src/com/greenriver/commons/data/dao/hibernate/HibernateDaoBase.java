package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsFieldProperties;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsProperties;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

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

    // <editor-fold defaultstate="collapsed" desc="QueryArguments related methods">
    @SuppressWarnings("unchecked")
    protected Criteria createCriteriaFromQueryArguments(
             EntityQueryArguments queryArguments) {

        Class argumentClass = queryArguments.getClass();
        QueryArgumentsProperties queryProperties =
                (QueryArgumentsProperties) argumentClass.getAnnotation(
                QueryArgumentsProperties.class);



        if (queryProperties == null) {
            throw new RuntimeException(
                    "The passed query argument is not annotated with @QueryArgumentsProperties");
        }

        // The criteria for the target class is created
        Criteria crit = getCurrentSession().createCriteria(
                queryProperties.targetClass());

        

        setBasicCriteriaParameters(crit, queryArguments,queryProperties);

        // For the fields of the query argument annotated with @QueryArgumentsFieldProperties        
        for (Field queryArgumentField : argumentClass.getDeclaredFields()) {
            addFieldRestriction(crit, queryArgumentField, queryArguments);
        }

        return crit;
    }

    protected Criteria createPaginatedCriteriaFromQueryArguments(
            int page,
            int pageSize,
            EntityQueryArguments queryArguments) {
        Criteria crit = createCriteriaFromQueryArguments(queryArguments);
        // We set the pagination values
        crit.setMaxResults(pageSize);
        crit.setFirstResult(page * pageSize);

        return crit;
    }

    private Object getValueForField(
            Field queryArgumentField,
            EntityQueryArguments queryArguments) {
        String methodName = "get" + Strings.toUpperCase(
                queryArgumentField.getName(), 0, 1);
        Method accesorMethod = null;
        Object value = null;
        try {
            accesorMethod = queryArguments.getClass().getMethod(methodName);
            value = accesorMethod.invoke(queryArguments);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
        return value;
    }

    private void setBasicCriteriaParameters(
            Criteria crit,
            EntityQueryArguments queryArguments,
            QueryArgumentsProperties queryProperties) {
        

        // We manage the sorting field.
        if (!Strings.isNullOrEmpty(queryArguments.getSortFieldName())) {
            if (queryArguments.isSortAscending()) {
                crit.addOrder(Order.asc(queryArguments.getSortFieldName()));
            } else {
                crit.addOrder(Order.desc(queryArguments.getSortFieldName()));
            }
        }

        // We add a disjuntion of likes for the fields affected by the text filter.
        if (!Strings.isNullOrEmpty(queryArguments.getTextFilter())) {
            Disjunction disjunction = Restrictions.disjunction();

            for (String fieldName : queryProperties.textFilterFields()) {
                disjunction.add(Restrictions.ilike(
                        fieldName,
                        queryArguments.getTextFilter(), MatchMode.ANYWHERE));
            }

            crit.add(disjunction);
        }
    }

    private void addFieldRestriction(
            Criteria crit,
            Field queryArgumentField,
            EntityQueryArguments queryArguments) {        
        QueryArgumentsFieldProperties queryFieldProperties =
                queryArgumentField.getAnnotation(
                QueryArgumentsFieldProperties.class);
        if (queryFieldProperties == null) {
            // We skip non annotated fields
            return;
        }

        Object value = getValueForField(queryArgumentField, queryArguments);
        if(value==null){
            // We dont apply restrictions for null values.
            return;
        }

        String fieldName = queryFieldProperties.fieldName();
        // We add a condition based on the field specified comparison type.
        switch (queryFieldProperties.type()) {
            case EQUALS:
                crit.add(Restrictions.eq(fieldName, value));
                break;
            case GREATER_EQUALS:
                crit.add(Restrictions.ge(fieldName, value));
                break;
            case GREATER_THAN:
                crit.add(Restrictions.gt(fieldName, value));
                break;
            case LOWER_EQUALS:
                crit.add(Restrictions.le(fieldName, value));
                break;
            case LOWER_THAN:
                crit.add(Restrictions.lt(fieldName, value));
                break;
            case LIKE:
                crit.add(Restrictions.ilike(fieldName, value.toString(),
                        MatchMode.ANYWHERE));
                break;
        }
    }
    // </editor-fold>
}
