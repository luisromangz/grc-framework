package com.greenriver.commons.data.pagination;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.PagedResult;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

/**
 * <b><u>Experimental</u></b> Paged data query for MySql that uses a
 * forward-only ScrollableResultSet to query paged results.<br/>
 * This class is not suitable for singleton use.
 * @author Miguel Angel
 */
public class MySqlPagedDataQueryImpl implements PagedDataQuery {

    private Query query;
    private Session session;

    @Override
    public Query getQuery() {
        return query;
    }

    @Override
    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public void setQuery(String hql) {
        if (session == null) {
            throw new IllegalStateException("Session not initiallized");
        }
        this.query = session.createQuery(hql);
    }

    public MySqlPagedDataQueryImpl() {
    }

    public MySqlPagedDataQueryImpl(Session currentSession) {
        this.session = currentSession;
    }

    /**
     *
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws HibernateException if the scroll operation fails
     */
    @Override
    public PagedResult fetch(int pageNumber, int pageSize) {
        ScrollableResults scroll = query.scroll(ScrollMode.FORWARD_ONLY);
        PagedResult pagedResult = new PagedResult(0, null, -1);
        int numResults = -1;
        List result = null;

        // TODO: Wiser use of the scroll to get the results at the same time
        // we scroll forward. First go to first result and start getting them
        // until we reach the end of the result set or we get all we wanted.
        // Once all the results have been retrieved scroll to the end to know
        // the total amount of results.
        try {
            // If there are no rows in the db this throws an exception instead
            // of returning false (scroll.last), so then we don't know if there
            // are no results or if the query failed to execute :(
            if (scroll.last()) {
                numResults = scroll.getRowNumber() + 1;
            }
        } catch (HibernateException hex) {
            // HACK: Try to guess when the scroll operation was done over an empty
            // set and failed.
            if (!(hex.getCause() instanceof SQLException) ||
                    !Strings.equals("Illegal operation on empty result set.",
                        hex.getCause().getMessage())) {
                
                throw new HibernateException("Scroll operation failed.", hex);
            }
        } finally {
            if (scroll != null) {
                //Ensure that this get freed always
                scroll.close();
            }
        }

        if (numResults >= 0) {
            pagedResult.setTotalPages(
                    (int) Math.ceil((double) numResults / pageSize));
        }

        // If there are results to return go ahead, if not return the last
        // available page of data or nothing when there are no results available
        if (numResults > 0 && numResults > pageNumber * pageSize) {
            //TODO: Hibernate says that this is done in memory, damm!
            query.setMaxResults(pageSize);
            query.setFirstResult(pageNumber * pageSize);
            result = query.list();
        } else if (numResults > 0) {
            //TODO: Hibernate says that this is done in memory, damm!
            query.setMaxResults(pageSize);
            pagedResult.setPageNumber(pagedResult.getTotalPages() - 1);
            query.setFirstResult(pagedResult.getPageNumber() * pageSize);
            result = query.list();
        } else {
            result = new ArrayList(0);
        }

        pagedResult.setResult(result);

        return pagedResult;
    }
}
