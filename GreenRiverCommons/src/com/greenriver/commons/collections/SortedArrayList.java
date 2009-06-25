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
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorted array list.
 * The array list is kept sorted using ordered insertion. Items with same order
 * are not guaranteed to keep the relative order (sorting is not stable).
 *
 * This collection is not thread-safe.
 * @param <T> Type of the list to sort
 */
public class SortedArrayList<T> extends ArrayList<T> {
    private static final long serialVersionUID = 6731719380450650517L;

    private class DefaultComparator implements Comparator<T> {

        public int compare(T o1, T o2) {
            if (o1 instanceof Comparable) {
                return ((Comparable)o1).compareTo(o2);
            } else {
                throw new IllegalStateException("The type is doesn't " +
                        "implements Comparable and " +
                        "you haven't provided a Comparator implementation");
            }
        }
    }

    private Comparator<T> comparator;

    public SortedArrayList() {
        comparator = new DefaultComparator();
    }

    public SortedArrayList(int initialCapacity) {
        super(initialCapacity);
        comparator = new DefaultComparator();
    }

    public SortedArrayList(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public SortedArrayList(int initialCapacity, Comparator<T> comparator) {
        super(initialCapacity);
        this.comparator = comparator;
    }

    @Override
    public boolean add(T e) {
        int pos = 0;
        if (e instanceof Comparable) {
            pos = Collections.binarySearch(this, e, comparator);
        }

        if (pos < 0) {
            pos = - (pos + 1);
        }

        if (pos == size()) {
            return super.add(e);
        } else {
            super.add(pos, e);
            return true;
        }
    }
}
