
package com.greenriver.commons.collections;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Miguel Angel
 */
public class Sets {

    public static boolean isNullOrEmpty(Set set) {
        return set == null || set.isEmpty();
    }

    public static <T,R> Set<R> apply(Set<T> set, ApplicableCommand<T,R> command){
        return applyIf(set, command, new FilteringCondition<T>(){

            @Override
            public boolean condition(T element) {
                return true;
            }

        });
    }

    public static <T,R> Set<R> applyIf(
            Set<T> set,
            ApplicableCommand<T, R> applicableCommand,
            FilteringCondition<T> filteringCondition) {
        Set<R> resultSet = new HashSet<R>();
        for(T setElement : set) {
            if(filteringCondition.condition(setElement)){
                resultSet.add(applicableCommand.apply(setElement));
            }            
        }

        return resultSet;
    }

    public static <T> Set<T> filter(Set<T> set, FilteringCondition<T> condition) {
        return applyIf(set, new ApplicableCommand<T, T>(){

            @Override
            public T apply(T element) {
                return element;
            }

        },condition);
    }

}
