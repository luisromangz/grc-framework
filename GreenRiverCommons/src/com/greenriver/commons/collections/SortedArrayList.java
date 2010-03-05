/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.collections;

import java.io.Serializable;
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
    private class ComparableComparator<T> implements Comparator<T>, Serializable {

        @Override
        public int compare(T o1, T o2) {
            if (o1 instanceof Comparable) {
                return ((Comparable) o1).compareTo(o2);
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

    public SortedArrayList(Collection<? extends T> source) {
        this(source, null);
    }

    public SortedArrayList(Collection<? extends T> source, Comparator<T> comparator) {
        super(source.size());
        this.comparator = comparator;

        this.addAll(source);
    }

    private int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return fallbackComparator.compare(a, b);
        }
    }

    private int internalIndexOf(T e) {
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

        return pos;
    }

    @Override
    public int indexOf(Object o) {

        // This can throw exceptions if type of o can be cast to T, but we can't
        // check nothing here without using a second parameter for the type of T
        // but that information is not here :( (remember type erasure).
        int pos = this.internalIndexOf((T)o);
        
        if (pos >= 0) {
            return pos;
        }

        return -1;
    }

    @Override
    public boolean add(T e) {
        int pos = this.internalIndexOf(e);

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
        int pos = this.internalIndexOf(element);
        
        if (pos < 0) {
            pos = -(pos + 1);
            if (pos == index) {
                super.add(pos, element);
            } else {
                throw new IllegalArgumentException(
                        "Inserting an element out of order is not allowed");
            }
        } else {
            super.add(pos, element);
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {

        boolean result = false;
        for (T element : collection) {
            result |= this.add(element);
        }

        return result;
    }

    protected boolean addAt(int pos, T element) {
        return this.addAt(pos, element, false);
    }

    /**
     * Inserts an element into a position checking that is ordered with the
     * previous and next elements only.
     * @param pos
     * @param element
     * @param throwExWhenNotAdded If true the method throws an exceptio when
     * the element can't be added
     * @return true if the element is sorted and then is added, or false if it
     * is not in order.
     */
    protected boolean addAt(int pos, T element, boolean throwExWhenNotAdded) {
        if (pos < 0 || pos > this.size()) {
            throw new ArrayIndexOutOfBoundsException("The index " + pos +
                    " is out of the bounds of this collection.");
        }

        boolean result = false;

        if (this.size() == 0) {
            result = super.add(element);
        } else if (pos == this.size() && compare(element, this.get(pos - 1)) > 0) {
            result = super.add(element);
        } else if (pos == 0 && compare(element, this.get(0)) < 0) {
            super.add(0, element);
            result = true;
        } else if (pos > 0 && pos < this.size() &&
                compare(element, this.get(pos - 1)) > 0 &&
                compare(element, this.get(pos + 1)) < 0) {
            super.add(pos, element);
            result = true;
        }

        if (!result && throwExWhenNotAdded) {
            throw new IllegalStateException("Can't insert element into position " + pos);
        }

        return result;
    }

    @Override
    public boolean remove(Object o) {
        // Use faster search to look for the item
        // If o if not of type T this throws an exception
        int pos = internalIndexOf((T)o);
        if (pos >= 0) {
            remove(pos);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        int pos = 0;

        for (Object obj : c) {
            pos = internalIndexOf((T)obj);
            if (pos >= 0) {
                remove(pos);
                modified = true;
            }
        }
        
        return modified;
    }

    public T getFirst() {
        if (this.isEmpty()) {
            return null;
        }

        return this.get(0);
    }

    public T getLast() {
        if (this.isEmpty()) {
            return null;
        }

        return this.get(this.size() - 1);
    }
}
