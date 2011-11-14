package com.greenriver.commons.data.dao.hibernate.base;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import com.greenriver.commons.data.dao.queryArgs.QueryArgsProps;
import com.greenriver.commons.data.dao.queryArgs.QueryArgsRestriction;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

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

    
    @Override
    public Criteria createCriteria(
            Class<? extends DataEntity> entityClass, 
            QueryArgs queryArguments,
            Criterion... restrictions) {

        return internalCreateCriteria(entityClass, queryArguments, true,restrictions);
    }

    @Override
    public Criteria createPagedCriteria(
            Class<? extends DataEntity> entityClass,
            QueryArgs queryArguments,
            Criterion... restrictions) {

        Criteria crit = internalCreateCriteria(
                entityClass, queryArguments, true,restrictions);
        // We set the pagination values
        crit.setFirstResult(queryArguments.getFirst());        
        crit.setMaxResults(queryArguments.getCount());

        return crit;
    }
    
    
    @Override
    public Criteria createCountingCriteria(
            Class<? extends DataEntity> entityClass, 
            QueryArgs entityQueryArguments,
            Criterion... restrictions) {
        Criteria crit = internalCreateCriteria(
                entityClass, entityQueryArguments, false,restrictions);
        
        crit.setProjection(Projections.countDistinct("id"));
        

        return crit;
    }

    
    // <editor-fold defaultstate="collapsed" desc="Auxiliary methods">
    private Criteria internalCreateCriteria(
            Class entityClass,
            QueryArgs queryArguments,
            boolean doSorting,
            Criterion... restrictions) {
       
        QueryArgsProps queryProperties =
                (QueryArgsProps) entityClass.getAnnotation(QueryArgsProps.class);

        if (queryProperties == null) {
            throw new RuntimeException(
                    "The passed query argument is not annotated with @QueryArgumentsProperties");
        }

        // The criteria for the target class is created
        Criteria crit = getSession().createCriteria(entityClass);
        crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        setTextFilter(crit, queryArguments, queryProperties);
        setRestrictions(entityClass, crit, queryArguments);

        if (doSorting) {
            setSorting(crit, queryArguments, queryProperties);
        }
        
        for (Criterion c: restrictions) {
            crit.add(c);
        }

        return crit;
    }

    private void setTextFilter(
            Criteria crit,
            QueryArgs queryArguments,
            QueryArgsProps queryProperties) {
        
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

                        disjunction.add(Restrictions.ilike(
                                field + "." + property,
                                queryArguments.getTextFilter(),
                                MatchMode.ANYWHERE));
                    }

                }
            }

            crit.add(disjunction);
        }
    }

    private void setRestrictions(
            Class entityClass,
            Criteria crit,
            QueryArgs queryArguments) throws SecurityException {
        // For the fields of the query argument annotated with @QueryArgsField
        for (QueryArgsRestriction restriction : queryArguments.getRestrictions()) {
            addFieldRestriction(entityClass, crit, restriction);
        }
    }

    private void addFieldRestriction(
            Class entityClass,
            Criteria crit,
            QueryArgsRestriction restriction) {
        
        String fieldName = restriction.getField();
        Object value = restriction.getValue();
//        try {
//            value = getRestrictionValue(entityClass, fieldName,restriction.getValue());
//        } catch (NoSuchFieldException ex) {
//            throw new RuntimeException(ex);
//        } catch (ParseException ex) {
//            throw new RuntimeException(ex);
//        }
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

        // We add a condition based on the field specified comparison type.
        switch (restriction.getOperator()) {
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
            default:
                throw new IllegalStateException(
                        "Operator "+ restriction.getOperator() + " not handled properly.");
        }
    }



    private void addSorting(Criteria crit, String fieldName, boolean ascending) {
        if (ascending) {
            crit.addOrder(Order.asc(fieldName));
        } else {
            crit.addOrder(Order.desc(fieldName));
        }
    }

    private void setSorting(
            Criteria crit,
            QueryArgs queryArgs,
            QueryArgsProps queryProperties) {

        boolean sortingSpecified = !Strings.isNullOrEmpty(queryArgs.getSortFieldName());
        if(sortingSpecified) {
            addSorting(crit, queryArgs.getSortFieldName(), queryArgs.isSortAscending());
        } else {
            // If no sorting was specified we try the default sorting stuff
            addDefaultSorting(crit, queryProperties);
        }
    }

    private void addDefaultSorting(Criteria crit, QueryArgsProps queryProperties) {
        if (queryProperties.defaultSortFields() == null
                || queryProperties.defaultSortFields().length == 0) {
            // Nothing to do if there isn't at least one field
            return;
        }

        if (queryProperties.defaultSortFields().length
                != queryProperties.defaultSortOrders().length) {
            throw new IllegalArgumentException("Sorting modes not defined for all default sort fields.");
        }

        for (int i = 0; i < queryProperties.defaultSortFields().length; i++) {
            this.addSorting(crit,
                    queryProperties.defaultSortFields()[i],
                    queryProperties.defaultSortOrders()[i]);

        }


    }

    /*private Object getRestrictionValue(
            Class entityClass, String fieldName,String value)
            throws NoSuchFieldException, ParseException {
        
       Class fieldClass=entityClass.getDeclaredField(fieldName).getType();
       
       if(fieldClass.isAssignableFrom(String.class)) {
           return value;
       } else if(fieldClass.isAssignableFrom(Long.class)){
           return Long.parseLong(value);
       } else if(fieldClass.isAssignableFrom(Double.class)) {
           return Double.parseDouble(value);
       } else if(fieldClass.isAssignableFrom(Date.class)) {
           return DateFormat.getInstance().parse(value);
       } else {
           throw new IllegalArgumentException("Not handled field type.");
       }
           
    }*/
}
// </editor-fold>

 