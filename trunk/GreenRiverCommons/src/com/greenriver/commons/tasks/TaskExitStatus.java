/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenriverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.com/licensing/greenriver-license

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

/**
 * Result of a task execution.
 * @author mangelp
 */
public enum TaskExitStatus {
    /**
     * Task was executed successfully.
     */
    SUCCESS,
    /**
     * Task was executed but with errors. No exception was thrown.
     */
    WARNING,
    /**
     * Task didn't succeed, there was an error but it was handled. Maybe the
     * task execution throwed an exception of type TaskException to be catched
     * upstream.
     */
    ERROR,
    /**
     * Task didn't succeed, there was an error that where undhandled and
     * the exception was catched upstream.
     */
    ERROR_OTHER,
    /**
     * No result available. Task was not executed.
     */
    NONE
}
