
package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.data.dao.queryArguments.QueryArgs;
import org.hibernate.Criteria;
import org.hibernate.Session;

/**
 * Operations to define a factory that builds criteria instances over specific
 * sources
 * @author Miguel Angel
 */
public interface CriteriaFactory {
    /**
     * Creates a criteria that returns the set of results that matches the
     * arguments used as query conditions.
     * @param entityQueryArguments
     * @return
     */
    Criteria createCriteria(QueryArgs entityQueryArguments);
    /**
     * Creates a criteria that returns a page of data from the total set of
     * results that could be returned using the arguments as query conditions
     * @param pageNum Number of the page to return
     * @param pageSize Number of results per page
     * @param entityQueryArguments
     * @return
     */
    Criteria createPagedCriteria(QueryArgs entityQueryArguments);
    /**
     * Creates a criteria that returns the total count of results that matches
     * the arguments used as query conditions. The criteria will return a single
     * integer value wrapped as an object.
     * @param entityQueryArguments
     * @return
     */
    Criteria createCountingCriteria(QueryArgs entityQueryArguments);
    /**
     * Gets the current session that this criteria factory is using to build
     * Criteria objects.
     * @return
     */
    Session getSession();
}
