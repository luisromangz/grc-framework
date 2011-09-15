package com.greenriver.commons.web.services;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the result given by a service call, so we can
 * return if the proccess was successful and an error message if it wasn't.
 * @param <T> The type of the contained payload.
 * @author luis
 */
public class PagedResult<T> extends ListResult<T> {

    private int total;
    
    public PagedResult() {
        super();
        this.setResult(new ArrayList<T>());
    }

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

   
}
