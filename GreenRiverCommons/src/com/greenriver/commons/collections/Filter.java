package com.greenriver.commons.collections;

/**
 * This interface defines conditions applicable to collections.
 * @author luis
 */
public interface Filter<T> {
    /**
     * The condition method applied to an element.
     * @param element The element being tested.
     * @param index The index of the element in the collection, or the order
     * in which the filter was applied, if the collection mantains no order.
     * @return True, if the condition on the element is true.
     */
    boolean condition(T element, int index);
}
