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
 * Abstract schedulerTask implementation with some common stuff.
 * @param <T> Type of task information
 * @param <D> 
 */
public abstract class BaseSchedulerTask<T extends TaskInfo, D extends TaskDescriptor>
        implements SchedulerTask<D>, Runnable  {
    private long interval = 1000;
    private boolean periodical;
    private boolean disabled;
    private int maxRetries = 0;
    private TaskScheduler scheduler;
    
    /**
     * @return the interval between executions
     */
    public long getInterval() {
        return interval;
    }

    /**
     * Sets the amount of time in milliseconds between task executions.
     * @param interval
     */
    public void setInterval(long interval) {
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval must be >= 0");
        }
        this.interval = interval;
    }

    /**
     * @return if the task is active (not disabled)
     */
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * @return if the task will repeat periodically
     */
    public boolean isPeriodic() {
        return periodical;
    }

    /**
     * @param active the active to set
     */
    public void setDisabled(boolean active) {
        this.disabled = active;
    }

    /**
     * @param periodical the repeated to set
     */
    public void setPeriodic(boolean periodical) {
        this.periodical = periodical;
    }

    public TaskPriority getPriority() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPriority(TaskPriority priority) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public TaskScheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }
}
