/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.ui.console;

/**
 * Operations that allow a command to store some of his own data in the session
 */
public interface ShellCommandData {

    /**
     * Returns a value by its name
     * @param name value to get
     * @return a value
     */
    public Object get(String name);

    /**
     * Sets a value by its name
     * @param name value to set
     * @param value the value that is being stored
     */
    public void set(String name, Object value);
}
