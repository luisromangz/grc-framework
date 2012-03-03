package com.greenriver.commons.web.services;

import java.util.*;

/**
 * Defines a service result encapsultator for services returning lists.
 * 
 * @author luisro
 */
public class ListResult <T> extends Result<List<T>>{

    public ListResult() {
        this.setResult(new ArrayList<T>());
    }
    
    /**
     * Adds an element to the result collection.
     * @param element 
     */
    public void add(T element) {
        this.getResult().add(element);                
    }
    
    /**
     * Adds all elements in the given collection to the result collection.
     * 
     * @param collection 
     */
    public void addAll(Collection<T> collection) {
        this.getResult().addAll(collection);
    }

    /**
     * Returns true if the given element is contained in the result collection.
     * @param o
     * @return 
     */
    public boolean contains(T o) {
        return getResult().contains(o);
    }

    /**
     * Sorts the result collection using the <c>compareTo</c> method defined
     * in the result elements' class. If said class is not Comparable, this will
     * fail.
     * 
     */
    public void sort() {
        this.sort(null);
    }
    
    /**
     * Sorts the result collection with the given comparator.
     * 
     * @param comparator 
     */
    public void sort(Comparator<T> comparator) {
        Collections.sort(this.getResult(), comparator);
    }
}
