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
 * Priorities for task queues.
 * @author mangelp
 */
public enum TaskPriority {
    /**
     * The task is queued in low-priority so it will be executed when anything
     * else is done.
     */
    LOW (4),
    /**
     * The task is queued in normal-priority so it will be executed when no more
     * high priority tasks are left.
     */
    NORMAL (8),
    /**
     * The task is queued in high-priority and will be done in the next iteration
     * of this queue.
     */
    HIGH (16);

    private int value;

    /**
     * Gets the value for the enumeration constant
     * @return an integer, the value of the constant
     */
    public int getValue() {
        return value;
    }

    private TaskPriority(int value) {
        this.value = value;
    }
}
