package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.DateRange;
import com.greenriver.commons.Dates;
import com.greenriver.commons.Strings;
import com.greenriver.commons.collections.Lists;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentSorting;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsFieldOrder;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsFieldProperties;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsProperties;
import com.greenriver.commons.data.dao.queryArguments.QueryArgumentsSortType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
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
 * <br/>
 * This factory uses criteria api to build the queries to run, but an error have
 * been detected when running these in hsqldb. The error is thrown when having
 * a count along with order by clauses in the same query.<br/>
 * This happens only in hsqldb as mysql runs them fine, and may be related due
 * to a bug in hsqldb or in hibernate hsqldb dialect's implementation. The
 * solution for this was to add a flag so when the total count of query results
 * is required we don't add sorting and otherwise we do it.
 * This stack overflow's thread may be related as well:
 * http://stackoverflow.com/questions/581043/sql-query-throws-not-in-aggregate-function-or-group-by-clause-exception
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
    @Override
    public Criteria createCriteriaFromQueryArguments(
            EntityQueryArguments queryArguments) {

        return internalCreateCriteriaFromQueryArguments(queryArguments, true);
    }

    @Override
    public Criteria createPaginatedCriteriaFromQueryArguments(
            int page,
            int pageSize,
            EntityQueryArguments queryArguments) {

        Criteria crit = internalCreateCriteriaFromQueryArguments(queryArguments, true);
        // We set the pagination values
        crit.setMaxResults(pageSize);
        crit.setFirstResult(page * pageSize);

        return crit;
    }

    private Criteria internalCreateCriteriaFromQueryArguments(
            EntityQueryArguments queryArguments, boolean doSorting) {
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

        setBasicCriteriaParameters(crit, queryArguments, queryProperties, doSorting);
        setRestrictions(argumentClass, crit, queryArguments);

        if (doSorting) {
            setSorting(crit, queryArguments, argumentClass, queryProperties);
        }

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
            QueryArgumentsProperties queryProperties,
            boolean doSorting) {

        // We manage the sorting field.
        if (doSorting && !Strings.isNullOrEmpty(queryArguments.getSortFieldName())) {
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
            // which hibernate despises.
            Map<String, List<String>> queryMappings =
                    new HashMap<String, List<String>>();

            for (String fieldName : queryProperties.textFilterFields()) {

                int index = fieldName.lastIndexOf(".");

                String field;
                String property = null;

                if (index >= 0) {
                    // In practice we are only supporting property.field at most.
                    field = fieldName.substring(0, index);
                    property = fieldName.substring(index + 1);
                } else {
                    field = fieldName;
                    property = null;
                }

                List<String> properties = null;
                if (!queryMappings.containsKey(field)) {
                    properties = new ArrayList<String>();
                    queryMappings.put(field, properties);
                } else {
                    properties = queryMappings.get(field);
                }

                if (property != null) {
                    properties.add(property);
                }
            }

            for (String field : queryMappings.keySet()) {
                List<String> properties = queryMappings.get(field);

                if (properties.isEmpty()) {
                    disjunction.add(Restrictions.ilike(field,
                            queryArguments.getTextFilter(),
                            MatchMode.ANYWHERE));
                } else {

                    crit.createAlias(field, field);

                    for (String property : properties) {

                        disjunction.add(Restrictions.ilike(field + "." + property,
                                queryArguments.getTextFilter(),
                                MatchMode.ANYWHERE));
                    }

                }
            }

            crit.add(disjunction);
        }
    }

    private void setRestrictions(Class argumentClass, Criteria crit, EntityQueryArguments queryArguments) throws SecurityException {
        // For the fields of the query argument annotated with @QueryArgumentsFieldProperties
        for (Field queryArgumentField : argumentClass.getDeclaredFields()) {
            addFieldRestriction(crit, queryArgumentField, queryArguments);
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

        if (value.getClass() == Integer.class || value.getClass() == Float.class || value.getClass() == Double.class) {
            // A better number detection would be necessary.
            if (value.toString().equals("NaN")) {
                return;
            }
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
                if (value.getClass() == Date.class
                        && queryFieldProperties.fullDay()) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(Dates.getDatePart((Date) value));
                    cal.add(Calendar.DATE, 1);
                    value = cal.getTime();
                    crit.add(Restrictions.lt(fieldName, value));
                } else {
                    crit.add(Restrictions.le(fieldName, value));
                }

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
                throw new IllegalStateException("Type "
                        + queryFieldProperties.type() + " not handled properly.");
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
        Criteria crit = internalCreateCriteriaFromQueryArguments(entityQueryArguments, false);
        crit.setProjection(Projections.rowCount());

        return crit;
    }

    private boolean addFieldSorting(
            Criteria crit,
            Field field,
            EntityQueryArguments queryArguments) {

        QueryArgumentsFieldOrder queryFieldProperties =
                field.getAnnotation(
                QueryArgumentsFieldOrder.class);

        if (queryFieldProperties == null) {
            // We skip non annotated fields
            return false;
        }

        Object value = getValueForField(field, queryArguments);
        if (value == null) {
            // We dont apply sorting for null values.
            return false;
        }

        if (QueryArgumentSorting.class.isAssignableFrom(value.getClass())) {
            this.addSorting(crit, (QueryArgumentSorting) value);
            return true;
        } else if (List.class.isAssignableFrom(value.getClass())) {
            return this.addSorting(crit, (List<QueryArgumentSorting>) value);
        } else {
            throw new IllegalStateException(
                    "The field " + field.getName() + " has a type not "
                    + "compatible with annotation QueryArgumentsFieldOrder.");
        }
    }

    private void addSorting(Criteria crit, QueryArgumentSorting sorting) {
        if (sorting.getType() == QueryArgumentsSortType.ASCENDING) {
            crit.addOrder(Order.asc(sorting.getFieldName()));
        } else {
            crit.addOrder(Order.desc(sorting.getFieldName()));
        }
    }

    private boolean addSorting(Criteria crit, List<QueryArgumentSorting> list) {
        if (Lists.isNullOrEmpty(list)) {
            return false;
        }

        for (QueryArgumentSorting sorting : list) {
            this.addSorting(crit, sorting);
        }

        return true;
    }

    private void setSorting(
            Criteria crit,
            EntityQueryArguments queryArguments,
            Class argumentClass,
            QueryArgumentsProperties queryProperties) {

        boolean sortingSpecified = false;
        boolean oldSortingSpecified = !Strings.isNullOrEmpty(queryArguments.getSortFieldName());

        // For the fields of the query argument annotated with @QueryArgumentsSorting
        for (Field queryArgumentField : argumentClass.getDeclaredFields()) {
            if (queryArgumentField.getAnnotation(QueryArgumentsFieldOrder.class) != null) {
                sortingSpecified = this.addFieldSorting(crit, queryArgumentField, queryArguments);

                if (sortingSpecified && oldSortingSpecified) {
                    throw new IllegalStateException(
                            "Can't add sorting from both argument specific "
                            + "single-field parameter and from configured field "
                            + "with QueryArgumentsSorting annotation");
                }

            }
        }

        if (!sortingSpecified && !oldSortingSpecified) {
            // If no sorting was specified we try the default sorting stuff
            addDefaultSorting(crit, queryProperties);
        }
    }

    private void addDefaultSorting(Criteria crit, QueryArgumentsProperties queryProperties) {
        if (queryProperties.defaultSortFields() == null || queryProperties.defaultSortFields().length == 0) {
            // Nothing to do if there isn't at least one field
            return;
        }

        if(queryProperties.defaultSortFields().length!=
                queryProperties.defaultSortTypes().length) {
            throw new IllegalArgumentException("Sorting modes not defined for all default sort fields.");
        }

        for (int i = 0; i < queryProperties.defaultSortFields().length; i++) {
            QueryArgumentSorting sorting = new QueryArgumentSorting(
                    queryProperties.defaultSortFields()[i],
                    QueryArgumentsSortType.ASCENDING);


            sorting.setType(queryProperties.defaultSortTypes()[i]);
            this.addSorting(crit, sorting);
        }

        
    }
}
// </editor-fold>

