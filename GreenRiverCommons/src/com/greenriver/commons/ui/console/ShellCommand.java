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
 * Operations of a single shell command to execute in the shell
 */
public interface ShellCommand {

    /**
     *
     * @return the name of the command
     */
    public String getName();

    /**
     *
     * @return the shell
     */
    public InteractiveShell getShell();

    /**
     * Gets the minimum argument number for this command (without the command
     * name)
     * @return the minimum arg number
     */
    public int getMinArgs();

    /**
     * Gets the maximum argument number for this command (without the command
     * name)
     * @return the maximum arg number
     */
    public int getMaxArgs();

    /**
     * Executes the command
     * @param args arguments for the command execution
     * @return 1 on succes, -1 on argument errors or -2 on other errors
     */
    public int exec(String[] args);

    /**
     * Print usage
     * @return
     */
    public String getUsage();

    /**
     * Print brief help
     * @return
     */
    public String getHelp();

    /**
     * Print help about a topic
     * @param topic
     * @return
     */
    public String getHelp(String topic);

    /**
     * Prints a message to output
     * @param msg message to print
     */
    public void out(String msg);

    /**
     * Reads a message
     * @param msg message to show before starting to read
     * @return the message read
     */
    public String in(String msg);

    /**
     * Prints an error message to output
     * @param msg
     */
    public void err(String msg);

    /**
     * Prints an error message to output with an stack tarce
     * @param msg message to print
     * @param t throwable cause of the error
     */
    public void err(String msg, Throwable t);
}
