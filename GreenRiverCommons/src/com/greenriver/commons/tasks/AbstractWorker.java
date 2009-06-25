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
 * Abstract worker implementation that implements Runnable and adds some
 * common stuff
 */
public abstract class AbstractWorker {

    private boolean finished;
    private boolean running;
    private boolean stopping;
    private boolean aborted;
    private String name;
    private Thread thread;

    /**
     * Gets if the execution was finished due to an exception.
     * @return
     */
    public boolean isAborted() {
        return aborted;
    }

    protected void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    /**
     * @return the task's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * If this unit is not executing as a timed task this property will return
     * the thread where it is executing.
     * @return The thread where this unit is executing or null if not.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * @return the finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Sets this work unit as finished
     * @param finished 
     */
    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Gets if the worker is running.
     * @return
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Gets if the worker is trying to stop.
     * @return
     */
    public boolean isStopping() {
        return stopping;
    }

    protected void setStopping(boolean stopping) {
        this.stopping = stopping;
    }

    /**
     * Main run method. Only to be called to execute the task directly.
     */
    public final void run() {
        //We get a hold on the thread where this unit is going to be executed
        //so shutdown() can interrupt it if called.
        thread = Thread.currentThread();
        running = true;
        
        try {
            init();
        } catch (RuntimeException ex) {
            stop();
            aborted = true;
            onRunAborted(ex);
        }

        try {
            if (!isFinished() && !isStopping()) {
                internalRun();
            }
        } catch (RuntimeException ex) {
            aborted = true;
            onRunAborted(ex);
            throw ex;
        } finally {
            try {
                cleanup();
            } catch (RuntimeException ex) {
                onRunAborted(ex);
            }

            running = false;
            //Remove the thread reference
            thread = null;
        }
    }

    /**
     * Error handler called after an exception aborted the execution of either
     * init, internalRun
     * @param ex
     */
    protected void onRunAborted(RuntimeException ex) {
    }

    /**
     * Inheritors must override this method to provide their own working
     * handling. This implementation simply calls work() implementation.
     */
    protected void internalRun() {
        work();
    }

    /**
     * Initiallization prior to execution. This method is called each time
     * the unit starts to run.
     */
    protected void init() {
    }

    /*
     * Real usefull work
     */
    protected abstract void work();

    /**
     * Method called after the working finishes. Do any cleanup here.<br/>
     * This method is called if the stop() method is called and the current
     * sleep time passes or the current call to work() finishes.
     *
     * It is not guaranteed that this method executes if the thread is aborted.
     */
    protected void cleanup() {
        finished = true;
        thread = null;
    }

    /**
     * Activates the flag to stop the unit after the end of the current
     * execytion cycle.<br/>
     * If the task is a timed task the timer is cancelled and his reference
     * removed.
     */
    public synchronized void stop() {
        stopping = true;
    }

    /**
     * Must cancel execution by any means.
     */
    public synchronized void shutdown() {
        try {
            stop();
        } catch (Exception ex) {
        }
        
        try {
            cleanup();
        } catch (Exception ex) {
        }
    }
}
