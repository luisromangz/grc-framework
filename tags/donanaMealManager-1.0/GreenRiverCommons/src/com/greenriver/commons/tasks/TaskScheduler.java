/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenriverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.com/licensing/greenriver-license

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

import java.util.logging.Logger;

/**
 * Contract operations for scheduler implementations.
 * set of tasks.
 * @param <T> Concrete type of tasks
 * @author mangelp
 */
public interface TaskScheduler<T extends SchedulerTask> {
    /**
     * Adds a new task
     * @param task
     * @return true if the task is added or false if it is rejected.
     */
    public boolean addTask(T task);

    /**
     * Tries to remove a task
     * @param task
     * @return true if the task is removed or false if it can't be removed by
     * any reason.
     */
    public boolean removeTask(T task);

    /**
     * Checks for the existance of a task in execution or waiting.
     * @param task
     * @return true if the task is executing or waiting. False if not.
     */
    public boolean hasTask(T task);

    /**
     * Returns the logger in use by the scheduler if any
     * @return a logger or null if no logging is being done.
     */
    public Logger getLogger();

    /**
     * Notifies the scheduler that a command have been finished. This will
     * update properly any internal information about the task.
     * @param task
     */
    void notifyFinished(T task);

    /**
     * Returns the interval of time that the scheduler sleeps between
     * schedule executions.
     * @return interval time of scheduler executions.
     */
    long getInterval();
}
