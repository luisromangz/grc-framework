/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.net;

/**
 *
 */
public class SerialSocketException extends RuntimeException {

    public SerialSocketException() {
        super();
    }

    public SerialSocketException(String msg) {
        super(msg);
    }

    public SerialSocketException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
