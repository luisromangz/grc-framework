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
 * Session handling operations. The session must store all the information of
 * the current session and allow the storage of command's data.
 */
public interface ShellSession {

    /**
     * checks a property
     * @param name property
     * @return if the property exists
     */
    public boolean hasProperty(String name);

    /**
     * @param name property
     * @return a property value
     */
    public Object getProperty(String name);

    /**
     * sets the property value
     * @param name property
     * @param value the value to set
     */
    public void setProperty(String name, Object value);

    /**
     * checks a flag
     * @param name flag to check
     * @return if the flag exists
     */
    public boolean hasFlag(String name);

    /**
     * gets the flag value
     * @param name the flag
     * @return the value
     */
    public boolean getFlag(String name);

    /**
     * sets the flag value
     * @param name the flag
     * @param flagValue the value
     */
    public void setFlag(String name, boolean flagValue);

    /**
     * Gets the storage for a command
     * @param command the command
     * @return the ShellCommandData instance for the command
     */
    public ShellCommandData data(ShellCommand command);

    /**
     *
     * @return if the interactive session will end
     */
    public boolean isExit();

    /**
     * sets the flag that will end the interactive session
     * @param value
     */
    public void setIsExit(boolean value);

    /**
     *
     * @return the history instance
     */
    public CommandHistory history();
}
