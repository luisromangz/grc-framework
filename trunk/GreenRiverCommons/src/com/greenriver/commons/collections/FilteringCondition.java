/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons.collections;

/**
 *
 * @author luis
 */
public interface FilteringCondition<T> {
    boolean condition(T element);
}
