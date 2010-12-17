package com.greenriver.commons.collections;

import java.util.Comparator;

/**
 *
 * @param <T>
 * @author luis
 */
public interface SortingCriteria<T> {
    float criteriaValue(T object);

    public static class Comparer<T> implements Comparator<T>{

        private SortingCriteria criteria ;

        public Comparer(SortingCriteria criteria) {
            this.criteria = criteria;
        }               

        @Override
        public int compare(T t, T t1) {
            return Float.compare(criteria.criteriaValue(t), criteria.criteriaValue(t1));
        }

    }
}
