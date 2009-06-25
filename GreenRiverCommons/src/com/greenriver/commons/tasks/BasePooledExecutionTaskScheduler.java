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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Abstract scheduler implementation that configures a thread pool to
 * concurrently execute tasks.<br/></br>
 * This class sets defaults to 1 minThread, 3 maxThreads, 8 maxTasks and a
 * keepAlive time of 5 seconds.
 * @param <T> Type of the tasks
 */
public abstract class BasePooledExecutionTaskScheduler <T extends SchedulerTask>
    extends IterativeWorker
    implements TaskScheduler<T>{
    
    private ThreadPoolExecutor executor;
    //Set some defaults
    private int minThreads = 1;
    private int maxThreads = 4;
    private int maxTasks = 8;
    private long keepAlive = 5000;
    private Logger logger;

    /**
     * Gets the minimum number of threads that the pool will contain even if
     * they are idle.
     * @return minimum number of threads.
     */
    public int getMinThreads() {
        return minThreads;
    }

    /**
     * Sets the minimum number of threads that the pool will contain even if
     * they are idle.
     * @param minThreads number of threads to set
     *  @throws IllegalArgumentException if minThreads less than 1
     */
    public void setMinThreads(int minThreads) {
        if (minThreads <= 0 || minThreads > maxThreads) {
            throw new IllegalArgumentException("minThreads must be >= 1 and " +
                    "less or equal than maxThreads.");
        }

        this.minThreads = minThreads;
    }

    /**
     * Sets the maximum number of threads that the pool will contain. Tasks
     * added when all the threads are busy are queued.
     * @return maximum number of threads.
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * Sets the maximum number of threads that the pool will contain. Tasks
     * added when all the threads are busy are queued.
     * @param maxThreads maximum number of threads.
     *  @throws IllegalArgumentException if maxThreads less than 1 or
     * maxThreads is less than minThreads.
     */
    public void setMaxThreads(int maxThreads) {
        if (maxThreads <= 0 || maxThreads < minThreads) {
            throw new IllegalArgumentException("maxThreads must be >= 1 and " +
                    "greater or equal than minThreads.");
        }
        this.maxThreads = maxThreads;
    }

    /**
     * Gets the maximum number of tasks that can be queued any time.
     * @return the maximum number of queued tasks.
     */
    public int getMaxTasks() {
        return maxTasks;
    }

    /**
     * Sets the maximum number of tasks that can be queued any time.
     * @param maxTasks maximum number of queued tasks.
     * @throws IllegalArgumentException if maxTasks less than 1
     */
    public void setMaxTasks(int maxTasks) {
        if (maxTasks <= 0) {
            throw new IllegalArgumentException("maximum tasks must be >= 1");
        }
        this.maxTasks = maxTasks;
    }

    /**
     * Gets the maximum time an idle thread is keept before removing it.
     * @return the timetoLive Maximum time an idle thread is kept.
     */
    public long getKeepAlive() {
        return keepAlive;
    }

    /**
     * Gets the maximum time an idle thread is keept before removing it.
     * @param keepAlive the time a thread will live if idle
     *  @throws IllegalArgumentException if keepAlive less than 100
     */
    public void setKeepAlive(long keepAlive) {
        if (keepAlive < 100) {
            throw new IllegalArgumentException("keep alive must be >= 100");
        }
        this.keepAlive = keepAlive;
    }

    /**
     * Gets the thread pool executor in use.
     * @return the executor in use.
     */
    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    /**
     * Default constructor.
     */
    public BasePooledExecutionTaskScheduler() {

    }

    @Override
    protected void init() {
        super.init();

        executor = new ThreadPoolExecutor(
                getMinThreads(),
                getMaxThreads(),
                getKeepAlive(),
                TimeUnit.MILLISECONDS,
                createQueue());

        //Try to start a core thread so it will be ready for a first task.
        executor.prestartCoreThread();
    }

    @Override
    public void stop() {
        super.stop();
        if (executor != null) {
            executor.shutdownNow();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                
            }
        }
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        executor = null;
    }

    /**
     * Creates the queue to be used by the pool. The queue type changes the
     * way the pool handle new tasks. See ThreadPoolExecutor documentation
     * before messing with this.
     * @return the queue to use with the pool to store tasks.
     */
    protected BlockingQueue<Runnable> createQueue() {
        if (getMaxTasks() <= 0) {
            throw new IllegalStateException("The maximum number of tasks " +
                    "can't be lower than 1. Have you configured them?");
        }
        return new ArrayBlockingQueue<Runnable>(this.getMaxTasks());
    }

    /**
     * Adds a new task to the executor
     * @param task Task to add
     * @throws TaskException If the task cannot be queued.
     * @return true if the task is added or false if not.
     */
    public boolean addTask(T task) {
        if (executor.getPoolSize() == getMaxTasks()) {
            throw new TaskException("Can't issue task. Queue full.");
        }

        if (task instanceof Runnable) {
            executor.execute((Runnable)task);
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
            return executor.remove((Runnable)task);
        } else {
            throw new IllegalStateException("The task doesn't implements Runnable");
        }
    }

    /**
     * Checks if a task is being executed
     * @param task
     * @return
     */
    public boolean hasTask(T task) {
        return executor.getQueue().contains(task);
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void notifyFinished(T task) {
        removeTask(task);
    }
}
