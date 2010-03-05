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
import java.util.Comparator;
import java.util.List;

/**
 * Utility methods that works over lists
 */
public class Lists {

    /**
     * Joins the elements of the list using the parameter glue as separator.
     * The elements of the list are converted to string using the toString
     * method.
     * @param list List whose elements are going to be joined.
     * @param glue
     * @return An string with all the elements of the first list converted to
     * strings and using the glue as separator.
     * @deprecated Use Strings.join, which operates over all kind of Collection.
     */
    @Deprecated
    public static String join(List list, String glue) {
        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder(list.size() * 4);

        sb.append(list.get(0) + "");

        for (int i = 1; i < list.size(); i++) {
            sb.append(glue + list.get(i) + "");
        }

        return sb.toString();
    }

    /**
     * Gets if a list is null or if it has zero elements.
     * @param list
     * @return true if the list is null or if there are no elements.
     */
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * Returns the element of maximum value of a given list regarding to a
     * defined criteria.
     * @param <T> The type of the elements of the List.
     * @param elements The list holding the elements from which the maximum will
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
     * @param elements The list holding the elements from which the minimum will
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
     * @param list List to iterate
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
     * @throws NullPointerException if any element is a null pointer.
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

    public static <T, R> List<R> apply(List<T> set, ApplicableCommand<T, R> command) {
        return applyIf(set, command, new FilteringCondition<T>() {

            @Override
            public boolean condition(T element) {
                return true;
            }
        });
    }

    public static <T, R> List<R> applyIf(
            List<T> set,
            ApplicableCommand<T, R> applicableCommand,
            FilteringCondition<T> filteringCondition) {
        List<R> resultSet = new ArrayList<R>();
        for (T setElement : set) {
            if (filteringCondition.condition(setElement)) {
                resultSet.add(applicableCommand.apply(setElement));
            }
        }

        return resultSet;
    }

    public static <T> List<T> filter(List<T> set, FilteringCondition<T> condition) {
        return applyIf(set, new ApplicableCommand<T, T>() {

            @Override
            public T apply(T element) {
                return element;
            }
        }, condition);
    }
}
