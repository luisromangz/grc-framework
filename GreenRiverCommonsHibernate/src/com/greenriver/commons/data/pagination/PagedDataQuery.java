
package com.greenriver.commons.data.pagination;

import com.greenriver.commons.data.PagedResult;
import org.hibernate.Query;

/**
 * <b><u>Experimental</u></b> Interface for paginated results fetch
 * @author Miguel Angel
 */
public interface PagedDataQuery {
    PagedResult fetch(int pageNumber, int pageSize);

    Query getQuery();

    void setQuery(Query query);

    void setQuery(String hql);
}
