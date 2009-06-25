/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

import com.greenriver.commons.Strings;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Repeatable work unit that executes periodically the work using a timer.
 */
public abstract class TimedWorker extends AbstractWorker {

    private long startDelay = 0L;

    /**
     * Gets the amount of milliseconds this timer will wait before doing the
     * first work call.
     * @return milliseconds to wait before first work call
     */
    public long getStartDelay() {
        return startDelay;
    }

    public void setStartDelay(long startDelay) {
        if (startDelay < 0) {
            throw new IllegalArgumentException(
                    "Start delay must be greater than 0");
        }
        this.startDelay = startDelay;
    }

    private class InternalTimedTask extends TimerTask {
        @Override
        public void run() {
            work();
        }
    }

    private Timer timer;
    private long interval;

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

    @Override
    public boolean isRunning() {
        return timer != null;
    }

    public TimedWorker() {
        super();
    }

    @Override
    protected void internalRun() {
        enableTimedTask();
    }

    /**
     * Enables the execution of this work unit as a timed task.
     */
    protected void enableTimedTask() {
        if (isRunning()) {
            throw new IllegalStateException("The task is running");
        }

        if (Strings.isNullOrEmpty(getName())) {
            timer = new Timer();
        } else {
            timer = new Timer(getName());
        }

        //The task will start just now
        timer.schedule(new InternalTimedTask(), startDelay, interval);
    }

    /**
     * Disables the execution of this work unit as a timed task.
     */
    protected void disableTimedTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void stop() {
        disableTimedTask();
    }
}
