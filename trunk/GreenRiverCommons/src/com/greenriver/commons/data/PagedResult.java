package com.greenriver.commons.data;


/**
 * @param <T> Type of the result
 * @author mangelp
 */
public class PagedResult<T> {
    private int totalPages;
    private T result;

    /**
     * @return the numPages
     */
    public int getTotalPages() {
	return totalPages;
    }

    /**
     * @param totalPages the numPages to set
     */
    public void setTotalPages(int totalPages) {
	this.totalPages = totalPages;
    }

    /**
     * @return the result
     */
    public T getResult() {
	return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(T result) {
	this.result = result;
    }

    public PagedResult() {
    }
}
