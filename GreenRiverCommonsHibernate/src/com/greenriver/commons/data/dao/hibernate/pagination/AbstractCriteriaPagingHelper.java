package com.greenriver.commons.data.dao.hibernate.pagination;

import com.greenriver.commons.data.PagedResult;
import com.greenriver.commons.data.dao.hibernate.CriteriaFactory;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import java.util.ArrayList;
import org.hibernate.Criteria;

/**
 * @author Miguel Angel
 */
public abstract class AbstractCriteriaPagingHelper implements CriteriaPagingHelper {

    private CriteriaFactory criteriaFactory;
    private EntityQueryArguments entityQueryArguments;

    @Override
    public CriteriaFactory getCriteriaFactory() {
        return criteriaFactory;
    }

    @Override
    public void setCriteriaFactory(CriteriaFactory criteriaFactory) {
        this.criteriaFactory = criteriaFactory;
    }

    @Override
    public EntityQueryArguments getEntityQueryArguments() {
        return this.entityQueryArguments;
    }

    @Override
    public void setEntityQueryArguments(EntityQueryArguments entityQueryArguments) {
        this.entityQueryArguments = entityQueryArguments;
    }

    /**
     * 
     * @param critFactory
     * @param args
     */
    public AbstractCriteriaPagingHelper(CriteriaFactory critFactory, EntityQueryArguments args) {
        this.criteriaFactory = critFactory;
        this.entityQueryArguments = args;
    }

    /**
     * Sets the results, the total page count and also sets the page number
     * in the result to the last available page if the requested page didn't
     * existed.
     * @param pagedResult
     * @param crit 
     * @param pageCount
     * @param pageNumber
     * @param pageSize
     */
    protected void setResults(PagedResult pagedResult, Criteria crit, int pageCount, int pageNumber, int pageSize) {
        pagedResult.setTotalPages(pageCount);
        // If there are results to return go ahead, if not return the last
        // available page of data or nothing when there are no results available
        if (pageCount > 0 && pageNumber < pageCount) {
            crit.setMaxResults(pageSize);
            crit.setFirstResult(pageNumber * pageSize);
            pagedResult.setResult(crit.list());
            pagedResult.setPageNumber(pageNumber);
        } else if (pageCount > 0) {
            pagedResult.setPageNumber(pagedResult.getTotalPages() - 1);
            crit.setMaxResults(pageSize);
            crit.setFirstResult(pagedResult.getPageNumber() * pageSize);
            pagedResult.setResult(crit.list());
        } else {
            pagedResult.setResult(new ArrayList(0));
        }
    }

    /**
     * Gets the count of results
     * @param pageSize 
     * @return
     */
    protected abstract int getPageCount(int pageSize);

    /**
     * Gets a criteria ready to return results
     * @param pageNumber
     * @param pageSize
     * @return
     */
     protected Criteria getListCriteria(int pageNumber, int pageSize) {
         CriteriaFactory critFactory = this.getCriteriaFactory();
         EntityQueryArguments entQueryArgs = this.getEntityQueryArguments();
         return critFactory.createPaginatedCriteriaFromQueryArguments(
                 pageNumber,
                 pageSize,
                 entQueryArgs);
    }

    @Override
    public PagedResult list(int pageNumber, int pageSize) {
        PagedResult pagedResult = new PagedResult();
        int pageCount = getPageCount(pageSize);

        Criteria crit = getListCriteria(pageNumber, pageSize);
        
        setResults(pagedResult, crit, pageCount, pageNumber, pageSize);

        return pagedResult;
    }
    
}
