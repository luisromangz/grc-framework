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
     * Task didn't succeed, The task execution throwed an exception.
     */
    ERROR,
    /**
     * No result available. Task is executing or waiting, but haven't finished.
     */
    NONE
}
