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
 * Operations to write to output
 * @author mangelp
 */
public interface OutputWriter {

    /**
     * Increases line padding
     */
    public void increasePad();

    /**
     * Decreases line padding
     */
    public void decreasePad();

    /**
     * Gets the pad string with the current padding
     * @return the pad string
     */
    public String getPad();

    /**
     *
     * @param msg message to write to output
     */
    public void write(String msg);

    /**
     *
     * @param msg message to write to output
     */
    public void writeln(String msg);

    /**
     * Writes an array of string separating them by linefeeds
     * @param msg messages to write to output
     */
    public void writeln(String[] msg);

    /**
     * Writes the stack trace of an exception to output
     * @param t Throwable cause of an exception
     */
    public void writeln(Throwable t);
}
