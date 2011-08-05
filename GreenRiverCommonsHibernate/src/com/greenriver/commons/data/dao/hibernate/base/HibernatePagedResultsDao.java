package com.greenriver.commons.data.dao.hibernate.base;

import com.greenriver.commons.data.DataEntity;
import com.greenriver.commons.data.dao.queryArgs.QueryArgs;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

/**
 * Base class for hibernate daos for entities that return multiple results, paged.
 * @author luis
 */
public class HibernatePagedResultsDao<T extends DataEntity, Q extends QueryArgs>
        extends HibernateMultipleResultsDao<T> {


    public int query(Q args, List<T> elements, Criterion... restrictions) {
        if (elements == null || !elements.isEmpty()) {
            throw new IllegalArgumentException("elements must be empty.");
        }

        Criteria crit = createPagedCriteria(args, restrictions);

        elements.addAll(crit.list());

        crit = createCountingCriteria(args, restrictions);

        Integer total = (Integer) crit.uniqueResult();

        return total;
    }
}
