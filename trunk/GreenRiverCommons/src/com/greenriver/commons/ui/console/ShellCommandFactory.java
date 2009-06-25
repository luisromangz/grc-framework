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
 * Operations for the factory that build command instances on-demand
 * @author mangelp
 */
public interface ShellCommandFactory {

    /**
     * Creates a command
     * @param name name of the command
     * @return the command instance or null if it doesn't exists
     */
    public ShellCommand create(String name);

    /**
     * Creates a command
     * @param args arguments for the command, the first must be the name
     * @return the command instance or null if it doesn't exists
     */
    public ShellCommand create(String[] args);
}
