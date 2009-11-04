
package com.greenriver.commons.collections;

/**
 *
 * @param <T> The class of the sortable elements.
 * @param <K> The class of the parameter.
 * @author luis
 */
public interface ParametrizableSortingCriteria<T,K>
        extends SortingCriteria<T>{ 

    /**
     * @return the parameter
     */
    public K getParameter();

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(K parameter);
}
