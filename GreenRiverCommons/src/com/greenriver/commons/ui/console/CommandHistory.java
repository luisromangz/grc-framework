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
 * Operations to manage a history of used commands and reuse them
 */
public interface CommandHistory {

    /**
     * @return the number of commands in the history
     */
    public int getSize();

/**
 * @return the maximum number of commands to hold
 */
    public int getMax();

    /**
     *
     * @return the current position of the history pointer
     */
    public int currentPosition();

    /**
     * Moves the history pointer to the previous command if any
     */
    public void goPrevious();

    /**
     * Moves the history pointer to the next command if any
     */
    public void goNext();

    /**
     * Moves the history pointer to the first command
     */
    public void goFirst();

/**
 * Moves the history pointer to the last command
 */
    public void goLast();

    /**
     * @return the previous command from the current history pointer
     */
    public String getPrevious();

    /**
     *
     * @return the current command from the history pointer
     */
    public String getCurrent();

    /**
     *
     * @return the next command from the history pointer
     */
    public String getNext();

    /**
     *
     * @return the first command if any
     */
    public String getFirst();

    /**
     *
     * @return the last command if any
     */
    public String getLast();

    /**
     * Adds a new command in the next position of the history buffer
     * @param cmd Command to add
     */
    public void add(String cmd);
}
