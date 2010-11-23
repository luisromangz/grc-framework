/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.web.helpers.form;

/**
 * Exception caused by a problem found while building a form.
 */
public class FormBuildingException extends RuntimeException {

    public FormBuildingException() {
	super();
    }

    public FormBuildingException(String msg) {
	super(msg);
    }

    public FormBuildingException(String msg, Throwable t) {
	super(msg, t);
    }
}
