package com.greenriver.commons.data;


/**
 * @param <T> Type of the result
 * @author Miguel Angel
 */
public class PagedResult<T> {
    // Total number of pages
    private int totalPages;
    // Result set
    private T result;
    // Returned page number
    private int pageNumber = -1;

    /**
     * Gets how many pages of data are available
     * @return the numPages
     */
    public int getTotalPages() {
	return totalPages;
    }

    /**
     * Sets how many pages of data are available
     * @param totalPages the numPages to set
     */
    public void setTotalPages(int totalPages) {
	this.totalPages = totalPages;
    }

    /**
     * Gets the result set
     * @return the result
     */
    public T getResult() {
	return result;
    }

    /**
     * Sets the result set
     * @param result the result to set
     */
    public void setResult(T result) {
	this.result = result;
    }

    /**
     * Gets the returned page number (0-based). If the requested page was not
     * present this will be the number of the returned page of data.
     * If there was no data (totalPages is 0) or this value is -1 (the default
     * value) this value must be ignored and the client must use the number
     * of the requested page.
     * @return the returned page number
     */
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public PagedResult() {
    }

    public PagedResult(int totalPages, T result, int pageNumber) {
        this.totalPages = totalPages;
        this.result = result;
        this.pageNumber = pageNumber;
    }
}
