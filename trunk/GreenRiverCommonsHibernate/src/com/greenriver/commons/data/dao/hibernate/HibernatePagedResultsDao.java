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

    public List<T> query(Q args, Criterion... criterions) {
        
        Criteria crit = createPagedCriteria(args);

        for(Criterion c : criterions) {
            crit.add(c);
        }

        return crit.list();
    }
}
