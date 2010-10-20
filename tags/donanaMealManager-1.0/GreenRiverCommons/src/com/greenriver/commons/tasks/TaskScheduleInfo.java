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
 * Information stored about the state of a task by the scheduler.
 * @author mangelp
 */
public interface TaskScheduleInfo {

    /**
     * Adds a value to the metric value
     * @param amount quantity to add to the current metric
     */
    void addMetric(long amount);

    /**
     * Adds the time to the total time of execution
     * @param totalExecutionTime
     */
    void addTotalExecutionTime(long totalExecutionTime);

    void addTotalRetries(long totalRetries);

    /**
     * @param totalWaitTime the totalWaitTime to set
     */
    void addTotalWaitTime(long totalWaitTime);

    /**
     * @return the executions
     */
    long getExecutions();

    long getFailedExecutions();

    /**
     * Get the last execution time in milliseconds
     * @return
     */
    long getLastExecutionTime();

    /**
     * @return the lastWaitTime
     */
    long getLastWaitTime();

    /**
     * Gets the maximum execution time in milliseconds
     * @return
     */
    long getMaxExecutionTime();

    /**
     * @return the maxWaitTime
     */
    long getMaxWaitTime();

    /**
     * Gets the current metric value
     * @return the metric
     */
    long getMetric();

    /**
     * @return the retries
     */
    int getRetries();

    /**
     * @return the current status of the task
     */
    TaskStatus getStatus();

    /**
     * Get total time this task have been executing.
     * @return
     */
    long getTotalExecutionTime();

    long getTotalRetries();

    /**
     * @return the totalWaitTime
     */
    long getTotalWaitTime();

    /**
     *
     */
    void incExecutions();

    void incFailedExecutions();

    /**
     *
     */
    void incRetries();

    void resetRetries();

    /**
     * Sets the time of the last execution
     * @param executionTime
     */
    void setLastExecutionTime(long executionTime);

    /**
     * @param lastWaitTime the lastWaitTime to set
     */
    void setLastWaitTime(long lastWaitTime);

    /**
     * Sets the maximum time of execution if the parameter is greater than the
     * last maximum time of execution.
     * @param executionTime
     */
    void setMaxExecutionTime(long executionTime);

    /**
     * @param maxWaitTime the maxWaitTime to set
     */
    void setMaxWaitTime(long maxWaitTime);

    /**
     * Sets the metric value
     * @param metric
     */
    void setMetric(long metric);

    /**
     * @param status
     */
    void setStatus(TaskStatus status);
    
}
