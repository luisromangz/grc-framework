package com.greenriver.commons.web.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Defines a service result encapsultator for services returning lists.
 * 
 * @author luisro
 */
public class ListResult <T> extends Result<List<T>>{

    public ListResult() {
        this.setResult(new ArrayList<T>());
    }
    
    public void add(T element) {
        this.getResult().add(element);                
    }
    
    public void addAll(Collection<T> collection) {
        this.getResult().addAll(collection);
    }

    public boolean contains(SelectOption o) {
        return getResult().contains(o);
    }
}
