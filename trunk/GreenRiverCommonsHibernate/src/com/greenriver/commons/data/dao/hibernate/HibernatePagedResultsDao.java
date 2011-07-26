package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

/**
 *
 * @author luis
 */
public class HibernatePagedResultsDao<T, Q extends QueryArgs>
        extends HibernateMultipleResultsDao<T> {

    public int query(Q args, List<T> elements, Criterion... restrictions) {
        if(elements==null || !elements.isEmpty()) {
            throw new IllegalArgumentException("elements must be empty.");
        }
        
        Criteria crit = createPagedCriteria(args,restrictions);

        elements.addAll(crit.list());
        
        crit = createCountingCriteria(args, restrictions);
     
        Integer total = (Integer) crit.uniqueResult();

        return total;
    }
}
