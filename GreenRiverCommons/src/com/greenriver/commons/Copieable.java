/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenriver.commons;

/**
 *
 * @author luis
 */
public interface Copieable<T> {
    void copyTo(T copyTarget);
}
