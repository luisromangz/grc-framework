
package com.greenriver.commons.services;

/**
 * ServiceResult extension that adds an extra field to store the number of
 * pages available.
 * @param <T> Type of the result
 * @author mangelp
 */
public class PagedServiceResult<T> extends ServiceResult<T> {

    private int totalPages;

    /**
     * @return the numPages
     */
    public int getTotalPages() {
	return totalPages;
    }

    /**
     * @param numPages the numPages to set
     */
    public void setTotalPages(int numPages) {
	this.totalPages = numPages;
    }

    public PagedServiceResult() {
	super();
    }
}
