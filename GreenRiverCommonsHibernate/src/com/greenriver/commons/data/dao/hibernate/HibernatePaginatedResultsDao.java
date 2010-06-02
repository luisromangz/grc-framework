package com.greenriver.commons.data.dao.hibernate;

import com.greenriver.commons.data.PagedResult;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;

/**
 *
 * @author luis
 */
public class HibernatePaginatedResultsDao<T, Q extends EntityQueryArguments>
        extends HibernateMultipleResultsDao<T> {

    public PagedResult<List<T>> getAllForPage(int page, int pageSize, Q args) {
        PagedResult<List<T>> pagedResult = new PagedResult<List<T>>();


        List<T> result = new ArrayList<T>();
        Criteria crit = createCriteriaFromQueryArguments(args);

        int numResults = 0;

        ScrollableResults scroll = crit.scroll();
        try {
            // If there are no rows in the db this throws an exception instead
            // of returning false (scroll.last), so then we don't know if there
            // are no results or if the query failed to execute :(
            if (scroll.last()) {
                numResults = scroll.getRowNumber();
            }
        } catch (HibernateException hex) {
        } finally {
            //Ensure that this get freed always
            scroll.close();
        }

        crit.setMaxResults(pageSize);
        crit.setFirstResult(page * pageSize);
        result = crit.list();

        pagedResult.setTotalPages(
                (int) Math.ceil((double) numResults / pageSize));

        pagedResult.setResult(result);

        return pagedResult;
    }
}
