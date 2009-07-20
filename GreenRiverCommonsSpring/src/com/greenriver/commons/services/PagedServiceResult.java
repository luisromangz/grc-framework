
package com.greenriver.commons.services;

/**
 * ServiceResult extension that adds an extra field to store the number of
 * pages available.
 * @param <T> Type of the result
 * @author mangelp
 */
public class PagedServiceResult<T> extends ServiceResult<T> {

    private int numPages;

    /**
     * @return the numPages
     */
    public int getNumPages() {
	return numPages;
    }

    /**
     * @param numPages the numPages to set
     */
    public void setNumPages(int numPages) {
	this.numPages = numPages;
    }

    public PagedServiceResult() {
	super();
    }
}
