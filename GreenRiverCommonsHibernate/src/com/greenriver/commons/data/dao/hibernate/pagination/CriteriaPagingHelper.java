
package com.greenriver.commons.data.dao.hibernate.pagination;

import com.greenriver.commons.data.PagedResult;
import com.greenriver.commons.data.dao.hibernate.CriteriaFactory;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;

/**
 * Helper that does the paging stuff for you to simplify your code
 * @author Miguel Angel
 */
public interface CriteriaPagingHelper {

    /**
     * If the factory was specified this method returns it. Otherwise returns
     * null
     * @return
     */
    CriteriaFactory getCriteriaFactory();

    /**
     * Sets the criteria factory that will be used to build the needed criterias
     * @param criteriaFactory
     */
    void setCriteriaFactory(CriteriaFactory criteriaFactory);

    EntityQueryArguments getEntityQueryArguments();

    /**
     * Sets the query arguments to use when building the criteria with the
     * factory.
     * @param entityQueryArguments 
     */
    void setEntityQueryArguments(EntityQueryArguments entityQueryArguments);
    
    /**
     * Returns all the results in the page
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PagedResult list(int pageNumber, int pageSize);
}
