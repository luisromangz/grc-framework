
package com.greenriver.commons.tasks;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This class sets defaults to 1 minThread, 3 maxThreads, 8 maxTasks and a
 * keepAlive time of 5 seconds.
 * @author mangelp
 */
public class PooledThreadExecutorHelper {
    
    //Thread pool where all the tasks goes into queue or execution
    private ThreadPoolExecutor executor;

    //Set some defaults
    private int minThreads = 1;
    private int maxThreads = 4;
    private int maxTasks = 8;
    private long keepAlive = 5000;
    private BlockingQueue<Runnable> queue;
    private RejectedExecutionHandler rejectedExecutionHandler;

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

    public BlockingQueue<Runnable> getQueue() {
	return queue;
    }

    public void setQueue(BlockingQueue<Runnable> queue) {
	this.queue = queue;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedTaskHandler) {
	this.rejectedExecutionHandler = rejectedTaskHandler;
    }

    public PooledThreadExecutorHelper() {
    }

    protected void assertExecutor() {
	if (executor == null) {
	    throw new IllegalStateException(
		    "The init() method must be called before using the helper");
	}
    }

    public int getPoolSize() {
	assertExecutor();
	return executor.getPoolSize();
    }

    public void execute(Runnable runnable) {
	assertExecutor();
	executor.execute(runnable);
    }

    public boolean remove(Runnable runnable) {
	assertExecutor();
	return executor.remove(runnable);
    }

    public void init() {
        executor = new ThreadPoolExecutor(
                getMinThreads(),
                getMaxThreads(),
                getKeepAlive(),
                TimeUnit.MILLISECONDS,
                queue);

	executor.setRejectedExecutionHandler(rejectedExecutionHandler);

        //Try to start a core thread so it will be ready for a first task.
        executor.prestartCoreThread();
    }

    public void stop() {
        if (executor != null) {
            executor.shutdownNow();
            try {
                executor.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {

            }
        }
    }

    public void cleanup() {
        executor = null;
    }
}
