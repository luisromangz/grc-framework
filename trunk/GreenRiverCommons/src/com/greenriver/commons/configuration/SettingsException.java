/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.configuration;

/**
 *
 */
public class SettingsException extends RuntimeException {

    public SettingsException() {
        super();
    }

    public SettingsException(String msg) {
        super(msg);
    }

    public SettingsException(String msg, Throwable t) {
        super(msg, t);
    }
}
