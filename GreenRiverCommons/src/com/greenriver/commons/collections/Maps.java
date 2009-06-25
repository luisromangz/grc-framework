/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for Map implementations
 */
public class Maps {

    /**
     * Serializes a map into a string using separators
     * @param data Map to serialize
     * @param pairSeparator Separator for pairs
     * @param valueSeparator Separator for a key-value
     * @return a String with the map serialized
     */
    public static String toString(Map<String, String> data,
            String pairSeparator,
            String valueSeparator) {
        StringBuilder strBuild = new StringBuilder();

        boolean first = true;

        for (String key : data.keySet()) {
            if (!first) {
                first = true;
            } else {
                strBuild.append(pairSeparator);
            }

            strBuild.append(key + valueSeparator + data.get(key));
        }

        return strBuild.toString();
    }

    /**
     * Splits a string in pairs key-value and puts them into a map.
     *
     * This implementation uses a hashmap initiallized with capacity
     * for just all the pairs.
     * 
     * @param conStr String to split
     * @param pairSeparator Separator for pairs
     * @param valueSeparator Separator for key-value pair
     * @return a map
     */
    public static Map<String, String> fromString(String conStr,
            String pairSeparator,
            String valueSeparator) {

        String[] pieces = conStr.split(pairSeparator);
        Map<String, String> result = new HashMap<String, String>(pieces.length);
        String[] keyVal = null;

        for (String piece : pieces) {
            piece = piece.trim();
            keyVal = piece.split(valueSeparator);

            if (keyVal.length != 2) {
                throw new IllegalArgumentException("The string is not parseable");
            }

            result.put(keyVal[0].trim(), keyVal[1].trim());
        }

        return result;
    }
}
