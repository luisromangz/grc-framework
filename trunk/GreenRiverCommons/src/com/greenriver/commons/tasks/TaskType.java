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
 * Task types for scheduling.
 *
 * Realtime tasks need their own threads to keep up with the relative timming
 * and they must be done always.Optional tasks can be discarded if the system is
 * under heavy load. And critial tasks can be delayed under heavy load but they
 * must be done anyways.
 * 
 * @author mangelp
 */
public enum TaskType {
    /**
     * The task should be done anytime.
     */
    OPTIONAL (1),
    /**
     * The task can be done in time or later, but must be done anytime.
     */
    CRITICAL (4),
    /**
     * The task must be done in time.
     */
    REALTIME (8);

    private int value;

    /**
     * Returns the value of the enumeration constant
     * @return an integer, the value of the constant
     */
    public int getValue() {
        return value;
    }

    /**
     * Private constructor to disallow to be used from outside.
     * @param value
     */
    private TaskType(int value) {
        this.value = value;
    }
}
