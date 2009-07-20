package com.greenriver.commons.data;


/**
 * @author mangelp
 */
public class PagedResult<T> {
    private int numPages;
    private T result;

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
