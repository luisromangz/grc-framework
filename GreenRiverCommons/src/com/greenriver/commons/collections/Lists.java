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
import java.util.Comparator;
import java.util.List;

/**
 * Utility methods that works over lists
 */
public class Lists {


    /**
     * Gets if a list is null or if it has zero elements.
     * @param collection
     * @return true if the list is null or if there are no elements.
     */
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * Returns the element of maximum value of a given list regarding to a
     * defined criteria.
     * @param <T> The type of the elements of the List.
     * @param collection The list holding the elements from which the maximum will
     * be searched.
     * @param criteria The criteria used to determine the maximun value.
     * @return The element of the list with the maximum value.
     */
    public static <T> T getMaxElement(
            List<T> elements,
            SortingCriteria<T> criteria) {

        if (elements.isEmpty()) {
            return null;
        }

        float max = Float.NEGATIVE_INFINITY;
        T returnedElement = null;

        for (T element : elements) {
            float criteriaValue = criteria.criteriaValue(element);
            if (criteriaValue > max) {
                max = criteriaValue;
                returnedElement = element;
            }
        }

        return returnedElement;
    }

    /**
     * Returns the element of minimum value of a given list regarding to a
     * defined criteria.
     * @param <T> The type of the elements of the List.
     * @param collection The list holding the elements from which the minimum will
     * be searched.
     * @param criteria The criteria used to determine the minimum value.
     * @return The element of the list with the minimum value.
     */
    public static <T> T getMinElement(
            List<T> elements,
            SortingCriteria<T> criteria) {

        if (elements.isEmpty()) {
            return null;
        }

        float min = Float.POSITIVE_INFINITY;
        T returnedElement = null;

        for (T element : elements) {
            float criteriaValue = criteria.criteriaValue(element);
            if (criteriaValue < min) {
                min = criteriaValue;
                returnedElement = element;
            }
        }

        return returnedElement;
    }

    /**
     * Looks for an element in a list using a linear search. The comparator is
     * used to check if the element have been found (comparison == 0) or not.
     * @param <T> type of the element to look for
     * @param obj Element to find
     * @param collection List to iterate
     * @param comparator Comparator to check if the element have been found
     * @return index of the element if found or -1 if not found
     */
    public static <T> int indexOf(T obj, List<T> list, Comparator<T> comparator) {

        for (int i = 0; i < list.size(); i++) {
            if (comparator.compare(list.get(i), obj) == 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Compares two lists first by size and then comparing elements.
     * The element comparison relies on equals implementation so this will work
     * if the objects in the lists have a proper implementation of it.
     * If one or both the lists allows null values you can expect this method
     * to throw a null pointer exception in these cases.
     * @param listA
     * @param listB
     * @param structural If true the elements are supposed to be in the same
     * order, if false the elements of the first list are checked to exists in
     * the second list.
     * @return
     * @throws NullPointerException if forAny element is a null pointer.
     */
    public static boolean equals(
            List<? extends Object> listA,
            List<? extends Object> listB,
            boolean structural) {

        if ((listA == null && listB != null)
                || (listA != null && listB == null)
                || listA.size() != listB.size()) {
            return false;
        }

        Object objA = null;

        if (!structural) {
            for (int i = 0; i < listA.size(); i++) {
                objA = listA.get(i);

                if (!listB.contains(objA)) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < listA.size(); i++) {
                objA = listA.get(i);

                if (!objA.equals(listB.get(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static <T, R> List<R> apply(Collection collection, Applicable<T, R> command) {
        return applyIf(collection, command, new Filter<T>() {

            @Override
            public boolean condition(T element, int index) {
                return true;
            }
        });
    }

    public static <T, R> List<R> applyIf(
            Collection<T> list,
            Applicable<T, R> applicableCommand,
            Filter<T> filteringCondition) {
        List<R> resultList = new ArrayList<R>();

        int index = 0;
        for(T element : list) {
             if (filteringCondition.condition(element,index)) {
                resultList.add(applicableCommand.apply(element));
            }

            index ++;
        }     

        return resultList;
    }

    public static <T> List<T> filter(Collection collection, Filter<T> condition) {
        return applyIf(collection, new Applicable<T, T>() {

            @Override
            public T apply(T element) {
                return element;
            }
        }, condition);
    }

    /**
     * Checks if the given condition applies to any of the passed list's element.
     *
     * @param <T> The type of the list's elements.
     * @param collection The list containing the elements to be checked.
     * @param filteringCondition The condition that will be applied to the elements.
     * @return True, if for at least one element in the list the condition is true.
     */
    public static <T> boolean  forAny(Collection<T> collection, Filter<T> filteringCondition) {
        
       int index = 0;
        for(T element : collection) {
             if (filteringCondition.condition(element,index)) {
               return true;
            }

            index ++;
        }

        return false;
    }
}
