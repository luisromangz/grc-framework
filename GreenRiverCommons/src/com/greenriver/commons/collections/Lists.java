/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.collections;

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
     */
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
    public static <T> T getMaxElement(List<T> elements, MinMaxCriteria<T> criteria) {

        if(elements.isEmpty()) {
            return null;
        }
        
        float max = Float.NEGATIVE_INFINITY;
        T returnedElement = null;

        for(T element : elements) {
            float criteriaValue = criteria.criteriaValue(element);
            if(criteriaValue > max) {
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
    public static <T> T getMinElement(List<T> elements, MinMaxCriteria<T> criteria) {

        if(elements.isEmpty()) {
            return null;
        }
        

        float min = Float.POSITIVE_INFINITY;
        T returnedElement = null;

        for(T element : elements) {
            float criteriaValue = criteria.criteriaValue(element);
            if(criteriaValue < min) {
                min = criteriaValue;
                returnedElement = element;
            }
        }

        return returnedElement;
    }
}
