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
 * Status flags
 * @author mangelp
 */
public enum TaskStatus {
    /**
     * Task was interrupted abruptly while executing.
     */
    ABORTED,
    /**
     * The task is not active.
     */
    DISABLED,
    /**
     * Task is being executed.
     */
    EXECUTING,
    /**
     * Task was executed.
     */
    EXECUTED,
    /**
     * Task has not information about where it is or where it goes. This
     * must be the default value when initiallizing a variable of this type.
     */
    NONE,
    /**
     * Task is waiting to execute.
     */
    WAITING
}
