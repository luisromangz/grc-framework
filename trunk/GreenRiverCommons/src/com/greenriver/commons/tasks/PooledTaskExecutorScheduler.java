/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

/**
 * Scheduler implementation that configures a thread pool to concurrently
 * execute tasks. The scheduling of the tasks relies in the type of structure
 * used for the task queue. See ThreadPoolExecutor javadocs for more information.
 * @param <T> Type of the tasks. They must implement RunnableSchedulerTask.
 */
public class PooledTaskExecutorScheduler <T extends RunnableSchedulerTask>
    extends IterativeWorker
    implements TaskScheduler<T> {

    private PooledThreadExecutorHelper executorHelper;
    private Logger logger;

    public PooledThreadExecutorHelper getExecutorHelper() {
	return executorHelper;
    }

    /**
     * Gets a logger used by this task scheduler
     * @return
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Sets the logger that this task scheduler should use
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Default constructor.
     */
    public PooledTaskExecutorScheduler() {
	executorHelper = new PooledThreadExecutorHelper();
    }

    @Override
    protected void init() {
	super.init();
	executorHelper.setQueue(createQueue(executorHelper));
	executorHelper.init();
    }

    @Override
    public synchronized void stop() {
	super.stop();
	executorHelper.stop();
    }

    @Override
    protected void cleanup() {
	super.cleanup();
	executorHelper.cleanup();
    }

    /**
     * Creates the queue to use with the ThreadPoolExecutor
     * @param executorHelper
     * @return a queue
     */
    protected BlockingQueue<Runnable> createQueue(PooledThreadExecutorHelper executorHelper) {
	return new ArrayBlockingQueue<Runnable>(executorHelper.getMaxTasks());
    }

    /**
     * Adds a new task to the executor
     * @param task Task to add
     * @throws TaskException If the task cannot be queued.
     * @return true if the task is added or false if not.
     */
    public boolean addTask(T task) {
        if (executorHelper.getPoolSize() == executorHelper.getMaxTasks()) {
            throw new TaskException("Can't issue task. Queue full.");
        }

        if (task instanceof Runnable) {
            executorHelper.execute((Runnable)task);
        } else {
            throw new IllegalStateException("The task doesn't implements Runnable");
        }

        return true;
    }

    /**
     * Tries to remove a task
     * @param task Task to remove
     * @return true if task is removed (also execution is interrupted) or false
     * if task cannot be removed.
     */
    public boolean removeTask(T task) {
        if (task instanceof Runnable) {
            return executorHelper.remove((Runnable)task);
        } else {
            throw new IllegalStateException("The task doesn't implements Runnable");
        }
    }

    /**
     * Checks if a task is queued for execution or executing.
     * @param task
     * @return true if the task is in execution or executing, false otherwise.
     */
    public boolean hasTask(T task) {
        return executorHelper.getQueue().contains(task);
    }

    /**
     * Notifies that a task has finished and should be handled.
     * @param task Task finished
     */
    public void notifyFinished(T task) {
        removeTask(task);
    }

    @Override
    protected void work() {
	//The thread pool used cares about all the work, so this method is
	//empty.
    }
}
