/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.tasks;

/**
 *
 */
public class SimpleTaskInfo {

    private TaskStatus status = TaskStatus.NONE;
    private TaskExitStatus exitStatus = TaskExitStatus.NONE;
    private Throwable abortCause;
    private int retries;
    private long executions;
    private long metric;

    /**
     * @return the status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * @return the result
     */
    public TaskExitStatus getExitStatus() {
        return exitStatus;
    }

    /**
     * @param exitStatus the result to set
     */
    public void setExitStatus(TaskExitStatus exitStatus) {
        this.exitStatus = exitStatus;
    }

    /**
     * @return the abortCause
     */
    public Throwable getAbortCause() {
        return abortCause;
    }

    /**
     * @param abortCause the abortCause to set
     */
    public void setAbortCause(Throwable abortCause) {
        this.abortCause = abortCause;
    }

    /**
     * @return the retries
     */
    public int getRetries() {
        return retries;
    }

    /**
     */
    public void incRetries() {
        this.retries ++;
    }

    public void resetRetries() {
        this.retries = 0;
    }

    /**
     * @return the executions
     */
    public long getExecutions() {
        return executions;
    }

    /**
     */
    public void incExecutions() {
        this.executions++;
    }

    /**
     * @return the metric
     */
    public long getMetric() {
        return metric;
    }

    /**
     * @param amount quantity to add to the current metric
     */
    public void addMetric(long amount) {
        this.metric += amount;
    }

    public void setMetric(long metric) {
        this.metric = metric;
    }

    @Override
    public String toString() {
        return "TaskInfo(S){status: " + getStatus() +
                ", result: " + getExitStatus() + ", retries: " + getRetries() +
                ", metric: " + getMetric() + ", executions: " + getExecutions() + "}";

    }
}
