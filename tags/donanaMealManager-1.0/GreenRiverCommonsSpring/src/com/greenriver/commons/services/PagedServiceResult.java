
package com.greenriver.commons.services;

/**
 * ServiceResult extension that adds an extra field to store the number of
 * pages available.
 * @param <T> Type of the result
 * @author mangelp
 */
public class PagedServiceResult<T> extends ServiceResult<T> {

    private int totalPages;
    private int pageNumber = -1;

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

    /**
     * Gets the page number of the returned result if any. If the result is an
     * error this is -1.
     * @return
     */
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public PagedServiceResult() {
	super();
    }
}
