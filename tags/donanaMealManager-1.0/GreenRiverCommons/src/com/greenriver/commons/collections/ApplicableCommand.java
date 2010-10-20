package com.greenriver.commons.collections;

/**
 * @param <T> Target type of the method application
 * @param <R> Result of the application of the method
 * @author luis
 */
public interface ApplicableCommand<T, R> {

    /**
     * Method that is applied over an element of type T and returns the
     * result of type R. Mostly used to be applied over collections.
     * @param element
     * @return
     */
    public R apply(T element);
}
