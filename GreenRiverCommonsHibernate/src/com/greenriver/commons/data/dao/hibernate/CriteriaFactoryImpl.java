
package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.DateRange;
import com.greenriver.commons.Strings;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsFieldProperties;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsProperties;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * Criteria factory impl that references the dao that is using it.
 * @author Miguel Angel
 */
public class CriteriaFactoryImpl implements CriteriaFactory {

    private Session session;

    @Override
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public CriteriaFactoryImpl() {
    }

    CriteriaFactoryImpl(Session session) {
        this.session = session;
    }

    // <editor-fold defaultstate="collapsed" desc="QueryArguments related methods">
    @SuppressWarnings("unchecked")
    @Override
    public Criteria createCriteriaFromQueryArguments(
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
        Criteria crit = getSession().createCriteria(
                queryProperties.targetClass());

        setBasicCriteriaParameters(crit, queryArguments, queryProperties);

        // For the fields of the query argument annotated with @QueryArgumentsFieldProperties
        for (Field queryArgumentField : argumentClass.getDeclaredFields()) {
            addFieldRestriction(crit, queryArgumentField, queryArguments);
        }

        return crit;
    }

    @Override
    public Criteria createPaginatedCriteriaFromQueryArguments(
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

            // We create a mapping to avoid serveral retrievals for the same field
            // which hiberante despises.
            Map<String, List<String>> queryMappings =
                    new HashMap<String,List<String>>();

            for (String fieldName : queryProperties.textFilterFields()) {

                int index = fieldName.lastIndexOf(".");

                String field;
                String property=null;

                if(index>=0) {
                    field = fieldName.substring(0, index);
                    property = fieldName.substring(index+1);                   
                } else {
                    field=fieldName;
                    property = null;
                }

                List<String> properties = null;
                if(!queryMappings.containsKey(field)) {
                    properties = new ArrayList<String>();
                    queryMappings.put(field, properties);
                } else {
                    properties = queryMappings.get(field);
                }

                if(property!=null) {
                    properties.add(property);
                }
            }

            for(String field : queryMappings.keySet()) {
                List<String> properties = queryMappings.get(field);

                if(properties.isEmpty()) {
                    disjunction.add(Restrictions.ilike(field,
                            queryArguments.getTextFilter(),
                            MatchMode.ANYWHERE));
                } else {
                    Criteria subCriteria = crit.createCriteria(field);
                    Disjunction subDisjunction = Restrictions.disjunction();

                    subCriteria.add(subDisjunction);
                    for(String property : properties) {
                        subDisjunction.add(Restrictions.ilike(property,
                                queryArguments.getTextFilter(),
                                MatchMode.ANYWHERE));
                    }

                }
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
        if (value == null) {
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
            case IN_RANGE:
                this.addInRangeFieldRestriction(fieldName, value, crit);
                break;
            default:
                throw new IllegalStateException("Type " +
                        queryFieldProperties.type() + " not handled properly.");
        }
    }

    private void addInRangeFieldRestriction(String fieldName, Object value, Criteria crit) {
        DateRange range = null;

        if (value instanceof DateRange) {
            range = (DateRange) value;
        } else if (value instanceof Date) {
            range = new DateRange((Date) value);
        } else {
            throw new IllegalStateException(
                    "In range comparison type specified for a value that"
                    + " is not a date nor a date range.");
        }

        if (range.getMax() != null) {
            crit.add(Restrictions.le(fieldName, range.getMax()));
        } else if (range.getMin() != null) {
            crit.add(Restrictions.ge(fieldName, range.getMin()));
        }
    }

    @Override
    public Criteria createCountingCriteriaFromQueryArguments(EntityQueryArguments entityQueryArguments) {
        Criteria crit = createCriteriaFromQueryArguments(entityQueryArguments);
        crit.setProjection(Projections.rowCount());

        return crit;
    }
    // </editor-fold>
}
