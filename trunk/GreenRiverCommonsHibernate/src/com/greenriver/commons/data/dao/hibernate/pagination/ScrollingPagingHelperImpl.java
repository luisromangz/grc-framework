package com.greenriver.commons.data.dao.hibernate.pagination;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.PagedResult;
import com.greenriver.commons.data.dao.hibernate.CriteriaFactory;
import com.greenriver.commons.data.dao.queryArguments.QueryArgs;
import java.sql.SQLException;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;

/**
 * <b><u>Experimental</u></b> Paged data query that uses a forward-only
 * ScrollableResultSet to query paged results.<br/>
 * This class is not suitable for singleton use and is not suitable for those
 * SGBD (or JDBC drivers) that doesn't support scrollable results.
 * @author Miguel Angel
 */
public class ScrollingPagingHelperImpl 
        extends AbstractCriteriaPagingHelper
        implements CriteriaPagingHelper {

    public ScrollingPagingHelperImpl(CriteriaFactory critFactory, QueryArgs entityQueryArguments) {
        super(critFactory, entityQueryArguments);
    }
    
    protected ScrollableResults getForwardOnlyScroll() {
        Criteria crit = this.getCriteriaFactory().createCriteria(this.getEntityQueryArguments());
        return crit.scroll(ScrollMode.FORWARD_ONLY);
    }

    @Override
    protected int getPageCount(int pageSize) {
        ScrollableResults scroll = getForwardOnlyScroll();
        int numResults = -1;

        // TODO: Wiser use of the scroll to get the results at the same time
        // we scroll forward. First go to first result and start getting them
        // until we reach the end of the result set or we get all we wanted.
        // Once all the results have been retrieved scroll to the end to know
        // the total amount of results. The sad part is to put all those
        // records into objects as hibernate does...
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
            if (!(hex.getCause() instanceof SQLException)
                    || !Strings.equals("Illegal operation on empty result set.",
                    hex.getCause().getMessage())) {

                throw new HibernateException("Scroll operation failed.", hex);
            }
        } finally {
            if (scroll != null) {
                //Ensure that this get freed always
                scroll.close();
            }
        }

        return (int) Math.ceil((double) numResults / pageSize);
    }
}
