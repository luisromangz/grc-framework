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
 * Task scheduling status and schedule information only updateable for the one
 * that has the lock object.
 */
public class ExclusiveTaskInfo implements TaskInfo {

    private TaskStatus status = TaskStatus.NONE;
    private TaskExitStatus result = TaskExitStatus.NONE;
    private Throwable abortCause;
    private int retries = 0;
    private int executions = 0;
    private long metric;
    private Object lock = null;

    /**
     * @return the status
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     * @param lock
     */
    public void setStatus(TaskStatus status, Object lock) {
        if (this.lock != lock) {
            throw new IllegalAccessError("You are not allowed to modify " +
                    "this property");
        }
        this.status = status;
    }

    /**
     * @return the result
     */
    public TaskExitStatus getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(TaskExitStatus result, Object lock) {
        if (this.lock != lock) {
            throw new IllegalAccessError("You are not allowed to modify " +
                    "this property");
        }
        this.result = result;
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
    public void setAbortCause(Throwable abortCause, Object lock) {
        if (this.lock != lock) {
            throw new IllegalAccessError("You are not allowed to modify " +
                    "this property");
        }
        this.abortCause = abortCause;
    }

    /**
     * @return the retries
     */
    public int getRetries() {
        return retries;
    }

    /**
     * @param lock
     */
    public void incRetries(Object lock) {
        if (this.lock != lock) {
            throw new IllegalAccessError("You are not allowed to modify " +
                    "this property");
        }
        this.retries++;
    }

    /**
     * @return the executions
     */
    public int getExecutions() {
        return executions;
    }

    /**
     * @param lock
     */
    public void incExecutions(Object lock) {
        if (this.lock != lock) {
            throw new IllegalAccessError("You are not allowed to modify " +
                    "this property");
        }
        this.executions++;
    }

    /**
     * @return the metric
     */
    public long getMetric() {
        return metric;
    }

    /**
     * @param metric the metric to set
     */
    public void setMetric(long metric, Object lock) {
        if (this.lock != lock) {
            throw new IllegalAccessError("You are not allowed to modify " +
                    "this property");
        }
        this.metric = metric;
    }

    /**
     * Checks if the lock object is the same as the argument
     * @param lock
     * @return true if the lock object is the same (by reference).
     */
    public boolean isLock(Object lock) {
        return this.lock == lock;
    }

    public ExclusiveTaskInfo(Object lock) {
        if (lock == null) {
            throw new NullPointerException("Null lock not allowed");
        }
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "TaskInfo(E){status: " + getStatus() +
                ", result: " + getResult() + ", retries: " + getRetries() +
                ", metric: " + getMetric() + "}";

    }
}
