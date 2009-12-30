
package com.greenriver.commons.hibernate;

/**
 * Utility methods
 * @author Miguel Angel
 */
public class HibernateUtility {

    /**
     * Gets if two ids have the same value. If one or both id parameters are
     * null this method always returns false.
     * @param idA
     * @param idB
     * @return true if the two ids are the same or false if the differ.
     */
    public static boolean sameId(Long idA, Long idB) {
        if (idA == null || idB == null) {
            // If one or both ids are null we can only say that they are not
            // the same. Even if both are null that means the entities are
            // nos persisted so we can't say yes, so we say no.
            return false;
        }

        return idA.compareTo(idB) == 0;
    }
}
