
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
        Set<R> resultSet = new HashSet<R>();
        for(T setElement : set) {
            resultSet.add(command.apply(setElement));
        }

        return resultSet;
    }
}
