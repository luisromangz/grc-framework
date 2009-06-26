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
 * Repeatable work unit to be executed at regular intervals as a runable into
 * a thread or as a timed event. This implementation also supports retrying the
 * task if it fails.
 */
public abstract class IterativeWorker extends AbstractWorker
        implements Runnable {

    private long interval;
    private int retryCount;
    private int maxRetries;

    public int getRetryCount() {
        return retryCount;
    }

    protected void resetRetryCount() {
        retryCount = 0;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets the maximum number of times this worker will try to execute after
     * consecutive fails.
     * @param maxRetries
     */
    protected void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        if (maxRetries > 0) {
            retryCount = 0;
        } else {
            retryCount = maxRetries;
        }
    }

    /**
     * @return the milliseconds to sleep between executions
     */
    public long getInterval() {
        return interval;
    }

    /**
     * @param interval milliseconds to sleep between executions
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * Default constructor
     */
    public IterativeWorker() {
        super();
    }

    /**
     * Real run. First configures (init) then performs the work (work) while
     * it is not finished and it is not stopping (canceled). After each work
     * run it calls sleep method. Once the work is finished (or canceled) it
     * calls cleanup to perform a proper cleanup if needed.
     *
     */
    @Override
    protected void internalRun() {
		boolean execute = true;
        while(execute) {
			execute = false;
            setStopping(false);
            setFinished(false);
            while(!isFinished() && !isStopping()) {
                try {
                    work();
                } catch (RuntimeException ex) {
                    onRunAborted(ex);
                    stop();
                }

                try {
                    if (!isFinished() && !isStopping()) {
                        sleep();
                    }
                } catch (InterruptedException ex) {
                    stop();
                }
            }

            if (isAborted() && retryCount < maxRetries) {
				execute = true;
                setAborted(false);
                retryCount++;
				
                try {
                    restart();
                } catch (RuntimeException ex) {
                    onRunAborted(ex);
                    execute = false;
                    stop();
                }
            }
        }
    }

    protected void sleep() throws InterruptedException {
        Thread.sleep(interval);
    }

    /**
     * Performs necesary steps to put things in order after the task was
     * aborted and a retry is going to be attempted.
     */
    protected void restart() {
        cleanup();
        init();
    }
}
