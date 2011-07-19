
package com.greenriver.commons.data.dao.hibernate.pagination;

import com.greenriver.commons.data.dao.hibernate.CriteriaFactory;
import com.greenriver.commons.data.dao.queryArguments.QueryArgs;
import org.hibernate.Criteria;

/**
 * @author Miguel Angel
 */
public class CriteriaPagingHelperImpl 
    extends AbstractCriteriaPagingHelper
    implements CriteriaPagingHelper{

    public CriteriaPagingHelperImpl(CriteriaFactory critFactory, QueryArgs entityQueryArguments) {
        super(critFactory, entityQueryArguments);
    }

    @Override
    protected int getPageCount(int pageSize) {
        Criteria countCrit = this.getCriteriaFactory().createCountingCriteria(
                this.getEntityQueryArguments());

        int rowCount = (Integer)countCrit.uniqueResult();
        return (int) Math.ceil((double) rowCount / pageSize);
    }
}
