/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorted array list.
 * The array list is kept sorted using ordered insertion. Items with same order
 * are not guaranteed to keep the relative order (sorting is not stable).
 *
 * This collection is not thread-safe.
 *
 * When no comparator is specified the sorting is done with the comparable
 * implementation by the elements of the array. If they don't implement this
 * an exception is thrown.
 * @param <T> Type of the elements of the list
 */
public class SortedArrayList<T> extends ArrayList<T> {

    private static final long serialVersionUID = 6731719380450650517L;

    /**
     * Comparator that checks if the items implements comparable and then it
     * uses that for comparison. This comparator is used to call the binary
     * search method of Collections without having for force the type T to
     * implement Comparator. This way this list can be used with any kind of
     * element if you supply the appropiate comparator. If not you will get
     * an exception if the elements doesn't implement comparable.
     * @param <T>
     */
    private class ComparableComparator<T> implements Comparator<T> {

	public int compare(T o1, T o2) {
	    if (o1 instanceof Comparable) {
		return ((Comparable)o1).compareTo(o2);
	    } else {
		throw new IllegalArgumentException("Not comparable elements");
	    }
	}
    }

    private Comparator<T> comparator;
    private ComparableComparator<T> fallbackComparator;

    protected void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Gets the comparator in use
     * @return
     */
    public Comparator<T> getComparator() {
	return comparator;
    }

    public SortedArrayList() {
	super();
    }

    public SortedArrayList(int initialCapacity) {
	super(initialCapacity);
    }

    public SortedArrayList(Comparator<T> comparator) {
	super();
	this.comparator = comparator;
    }

    public SortedArrayList(int initialCapacity, Comparator<T> comparator) {
	super(initialCapacity);
	this.comparator = comparator;
    }

    @Override
    public boolean add(T e) {
	int pos = 0;

	if (comparator == null && !(e instanceof Comparable)) {
	    throw new IllegalStateException("The type doesn't " +
		    "implements Comparable and " +
		    "you haven't provided a Comparator implementation");
	}

	if (comparator != null) {
	    pos = Collections.binarySearch(this, e, comparator);
	} else {
	    //We need to use the fallback comparator here as we can't call this
	    //method without forcing in the generic type definition of T that
	    //it must implement Comparable
	    pos = Collections.binarySearch(this, e, fallbackComparator);
	}

	if (pos < 0) {
	    pos = -(pos + 1);
	}

	if (pos == size()) {
	    return super.add(e);
	} else {
	    super.add(pos, e);
	    return true;
	}
    }

    @Override
    public void add(int index, T element) {
        int pos = Collections.binarySearch(this, element, comparator);
        if (pos < 0) {
            pos = -(pos + 1);
            if (pos == index) {
                super.add(pos, element);
            } else {
                throw new IllegalArgumentException(
                        "Inserting an element out of order is not allowed");
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {

	boolean result = false;
	for (T element : collection) {
	    if (this.add(element)) {
		result = true;
	    }
	}

	return result;
    }
}
