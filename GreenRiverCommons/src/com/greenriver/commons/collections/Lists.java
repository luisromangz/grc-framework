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
}
