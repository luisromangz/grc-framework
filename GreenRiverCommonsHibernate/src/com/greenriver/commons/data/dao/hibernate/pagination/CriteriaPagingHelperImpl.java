
package com.greenriver.commons.data.dao.hibernate.pagination;

import com.greenriver.commons.data.dao.hibernate.CriteriaFactory;
import com.greenriver.commons.data.dao.queryArguments.EntityQueryArguments;
import org.hibernate.Criteria;

/**
 * @author Miguel Angel
 */
public class CriteriaPagingHelperImpl 
    extends AbstractCriteriaPagingHelper
    implements CriteriaPagingHelper{

    public CriteriaPagingHelperImpl(CriteriaFactory critFactory, EntityQueryArguments entityQueryArguments) {
        super(critFactory, entityQueryArguments);
    }

    @Override
    protected int getPageCount(int pageSize) {
        Criteria countCrit = this.getCriteriaFactory().createCountingCriteriaFromQueryArguments(this.getEntityQueryArguments());

        int rowCount = (Integer)countCrit.uniqueResult();
        return (int) Math.ceil((double) rowCount / pageSize);
    }
}
