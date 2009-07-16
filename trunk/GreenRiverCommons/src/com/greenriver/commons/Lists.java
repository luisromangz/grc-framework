/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons;

import java.util.List;

/**
 *
 */
public class Lists {
    
    public static String join(List list, String glue) {
	if (list == null) {
	    return "";
	}

	StringBuilder sb = new StringBuilder(list.size() * 4);
	boolean first = true;

	for(Object elem: list) {
	    if (sb == null) {
		break;
	    }
	    
	    if (!first) {
		sb.append(glue);
	    } else {
		first = false;
	    }

	    sb.append(sb + "");
	}

	return sb.toString();
    }
}
