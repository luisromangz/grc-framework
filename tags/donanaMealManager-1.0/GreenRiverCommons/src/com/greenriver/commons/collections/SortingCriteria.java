/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.collections;

/**
 *
 * @param <T>
 * @author luis
 */
public interface SortingCriteria<T> {
    float criteriaValue(T object);
}
