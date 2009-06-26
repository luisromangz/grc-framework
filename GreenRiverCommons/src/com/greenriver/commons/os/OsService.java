/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.os;

import com.greenriver.commons.Application;
import com.greenriver.commons.ShutdownManager;
import com.greenriver.commons.configuration.SettingsProvider;
import com.greenriver.commons.configuration.ApplicationSettingsProvider;
import com.greenriver.commons.log.LogHandlerType;
import com.greenriver.commons.log.SimplifiedFormater;
import com.greenriver.commons.tasks.IterativeWorker;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract os service support. Tries to implement the most of the required
 * support with only java code.
 * <br/>
 * This service implementation provides a lot of the functionality needed to
 * implement fastly other services. It cares about the pid file, the logging
 * and task retrying.<br/>
 * This implementation uses and configures the Application when an instance
 * is created, and creating two instances will cause an exception to be thrown
 * by Application setup method.
 *<br/><br/>
 * <b>References:</b><br/>
 * <ul><li>
 *  http://barelyenough.org/blog/2005/03/java-daemon/
 * </li></ul>
 * <br><b>Configuration:</b><br/>
 *  This service depends on a settings provider to obtain configuration. By
 *  default an empty simple settings provider is used where keys can be added
 *  but a new one can be set and used.
 * <br/><br/>
 *  Some of the keys that this class can use are:
 * <ul>
 *  <li>logPath : File name for log.
 *  </li></li>logSize : Maximum log file size if logging to file is
 * enabled
 *  </li></li>logNumber : Maximum number of log files to have
 *  </li></li>logLevel : One of the java.util.logging.Level constants name
 *  </li></li>logHandler : [file, console] Logging target.
 *  </li></li>pidFile : File name for pid.
 *  </li></li>isDaemon: If true detaches from console, if false doesn't
 *  detaches.
 *  </li></li>maxThreads: Sets the maximum number of threads to use.
 *  </li></li>minThreads: Sets the minimum number of threads to use. These
 *  number of threads are kept event without no work to do.
 *  </li><li>maxIdle: Sets the maximum number of idle threads.
 *  </li></li>maxQueue: Maximum number of queued tasks.
 *  </li></li>threadTTL: Sets the amount of time a thread is kept alive
 *  without being used.
 * </ul>
 * The current implementation doesn't do anything with threads so all the
 * related keys are not used.<br/>
 * This implementation configures the global Application with the settings it
 * has or if it haven't got one settings provider it will use the default
 * provider of Application. In any case this means that every key is rooted
 * into the Application root that is forced to be 'app'. So any key in the
 * configuration will read as app.KeyName so keep this in mind when writting it.
 */
public abstract class OsService extends IterativeWorker {

    /**
     * Configuration keys for the service
     */
    public enum Keys {

        /**
         * Tells if the service will run as a daemon (boolean). True detaches
         * the service form console.
         */
        DAEMON("isDaemon"),
        /**
         * Way of logging things. Can be one of: file, console, default.
         */
        LOGHANDLER("logHandler"),
        /**
         * Log file name (with path). Defaults to OsEnvironment.getLogPath()
         * using the name of the service as file name.
         */
        LOGFILE("logFile"),
        /**
         * Size of the log file before rotating (bytes).
         */
        LOGSIZE("logSize"),
        /**
         * Number of log files to keep (int)
         */
        LOGNUMBER("logNumber"),
        /**
         * Global logging level. One of: off, all, fine, finer, finest, info,
         * warning, severe.
         */
        LOGLEVEL("logLevel"),
        /**
         * Maximum number of queued task that this service can have (int).
         * If your service doesn't use this just ignore it.
         */
        MAXQUEUE("maxQueue"),
        /**
         * Maximum number of threads to use by the service (int). 
         * If your service doesn't spawn threads just ignore this.
         */
        MAXTHREADS("maxThreads"),
        /**
         * Minimun number of threads to use by the service when working (int).
         * If your service doesn't spawn threads just ignore this.
         */
        MINTHREADS("minThreads"),
        /**
         * Name for the service. Usually set to the script name IE dynipd.
         */
        NAME("name"),
        /**
         * Brief description of service purposse. Not really usefull.
         */
        DESCRIPTION("description"),
        /**
         * Pid file name. Defaults to OsEnvironment.getPidPath() using the name
         * of the service as file name.
         */
        PIDFILE("pidFile"),
        /**
         * Maximum time a thread will last without being working(long msec). If
         * your service doesn't use this just ignore it.
         */
        THREADTTL("threadTTL");
        private String value;

        public String getValue() {
            return value;
        }

        private Keys(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Implementation to handle gracefull shutdown of this service.
     */
    protected class OsServiceShutdownTask implements ShutdownManager {

        public void run() {
            shutdown();
        }
    }

    /**
     * Shared logger from the service
     * @return the logger instance
     */
    public static Logger logger() {
        return Application.get().getLogger();
    }

    /**
     * Changes the general logging level for the service logger.
     * @param level
     */
    public static void setLogLevel(Level level) {
        logger().setLevel(level);
    }
    private boolean daemon = false;
	private PidFileHandler pidFileHandler;
    private File logFile;
    private OsServiceController controller;
    private SettingsProvider settings;
    /**
     * Time to wait on shutdown for the thread to cleanup by itself.
     * If after this time it haven't finished it gets interrupted.
     */
    private long threadWaitTimeout = 1000;
    private Logger logger;
    private boolean controllerStopLoop = false;
    private OsServiceShutdownTask shutdownManager;

    /**
     * Gets a logger ready to use
     * @return
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Sets a logger to be used
     * @param logger
     */
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        if (settings != null) {
            return settings.get(Keys.NAME.value, getClass().getSimpleName());
        }
        return null;
    }

    @Override
    public void setName(String name) {
        if (settings == null) {
            throw new IllegalStateException(
                    "No settings available. Can't set name.");
        }
        settings.getStorage().set(Keys.NAME.value, name);
    }

    /**
     * @return the settings of this application
     */
    public SettingsProvider getSettings() {
        return settings;
    }

    /**
     * Sets the provider for settings to be used by this service. If this is
     * set after initiallization it will throw an exception as this provider
     * will be configured as the default one for the Application at init.
     * @param settings application settings
     */
    public void setSettings(SettingsProvider settings) {
        if (Application.isInitialized()) {
            throw new IllegalStateException(
                    "Can't set this property after the initiallization " +
                    "(init method call). Try to set this before.");
        }

        this.settings = settings;
    }

    /**
     * Gets the controller for this service
     * @return
     */
    protected OsServiceController getController() {
        return controller;
    }

    /**
     * Sets the controller to use in this service
     * @param controller
     */
    protected void setController(OsServiceController controller) {
        this.controller = controller;
    }

    /**
     * Gets if this service will run as a daemon in background.
     * @return the daemon flag
     */
    public boolean isDaemon() {
        if (settings == null) {
            return false;
        }
        return settings.getBool(Keys.DAEMON.value, false);
    }

    /**
     * @return the pidFile
     */
    public File getPidFile() {
		if (pidFileHandler != null) {
			return pidFileHandler.getPidFile();
		} else {
			return null;
		}
    }

    /**
     * Tries to return the pid of the process.
     * 
     * @see Application.getPid()
     * @return the pid of the current process on success, -1 on fail
     */
    public int getPid() {
        return Application.get().getPid();
    }

    /**
     * Initiallizes the default settings provider, the shutdown manager and
     * registers this instance within the application shutdown manager.
     */
    public OsService() {
        super();
        this.settings = new ApplicationSettingsProvider();
        shutdownManager = new OsServiceShutdownTask();
        Application.registerShutdownManager(shutdownManager);
    }

    /**
     * Detaches a process from standard in, out and error only if the daemon
     * flag is set.
     */
    protected void detach() {
        if (!daemon) {
            return;
        }

        if (logger != null) {
            logger.log(Level.INFO, "Detaching from console");
        }

        try {
            System.in.close();
        } catch (IOException ex) {
            if (logger != null) {
                logger.log(Level.SEVERE, "Error closing input", ex);
            }
        }

        System.out.close();
        System.err.close();
    }

    /**
     * Runs the service process and keeps working until stop() is called or the
     * flag stopRun is set internally.
     * <br/><br/>
     * Child classes can override this but is discouraged as a lot of exception
     * handling is done here to try to shut down things properly.
     */
    @Override
    protected void internalRun() {
        //If there is a controller put it to work
        if (controller != null) {
            try {
                runController(controller);
            } catch (Throwable t) {
                if (logger != null) {
                    logger.log(Level.SEVERE,
                            "Controller run failed.", t);
                } else {
                    System.out.println("Controller run failed.");
                    t.printStackTrace();
                }
            }
        }

        //Let the party begin
        super.internalRun();
    }

    /**
     * Enables the flag that stops this daemon after the next sleep
     */
    @Override
    public synchronized void stop() {
        super.stop();

        //If there is a controller stop it too.
        if (this.controller != null && !controllerStopLoop) {
            try {
                controllerStopLoop = true;
                this.controller.stop();
                controllerStopLoop = false;
            } catch (Throwable t) {
                if (logger != null) {
                    logger.log(Level.WARNING, "Controller stop failed", t);
                }
            }
            this.controller = null;
        }

        if (logger != null) {
            logger.finer("Service stop.");
        }
    }

    /**
     * Called after working loop has finished. Cleanup for the service.
     *
     * This implementation cares only about removing the pid file, if you
     * override this method call parent's one or care about the pid file
     * yourself.
     */
    @Override
    protected void cleanup() {
        super.cleanup();

		if (pidFileHandler != null) {
			pidFileHandler.release();
			pidFileHandler = null;
		}

        if (logger != null) {
            logger.finer("Service cleanup.");
        }
    }

    /**
     * Service shutdown. It first try to do a stop, then call cleanup and
     * finally awaits the thread to cleanup
     */
    @Override
    public synchronized void shutdown() {
		//The parent impl tries to do a stop() and then a clean()
        super.shutdown();

        //Wait for the thread to end
        try {
            if (getThread() != null) {
                //Wait our thread to cleanup
                getThread().join(threadWaitTimeout);
            }
        } catch (InterruptedException ex) {
            if (logger != null) {
                logger.fine("Shutdown interrupted");
            }
        }

        //If it haven't finished and nobody interrupted it let's do it
        if (getThread() != null && getThread().isAlive()
				&& !getThread().isInterrupted()) {
            //This doesn't abort the thread but is all that we can do without
            //using deprecated methods.
            getThread().interrupt();
        }

        if (logger != null) {
            logger.info("Service shutdown.");
        }

        if (!Application.get().isShuttingDown()) {
            //If this shutdown haven't been called by the application itself
            //this will shut down everything.
            Application.get().shutdown();
        }
    }

    /**
     * First method called to initialize everything. Create pid file, 
     * initiallize logging, detach from console, open sockets, create threads,
     * etc, ...
     */
    @Override
    protected void init() {
        //First of all configure logging so we can start using it.
        configureLogging();

        //if the application is already configured by this service we don't go
        //throught init again.
        if (Application.isInitialized() &&
                Application.get().getInitializer() == this) {
            return;
        } else if (Application.isInitialized()) {
            throw new IllegalStateException("Application already initiallized");
        }

        //We need to get the name and description for this service to configure
        //the application

        Application.setup(
                this,
                getName(),
                settings.get(Keys.DESCRIPTION.value, null),
                logger);

        //The settings for the application will be the ones we have if they
        //are set if not we use Application settings.
        if (settings != null) {
            Application.configure(this, settings);
        } else {
            settings = Application.settings();
        }

        //We use a temporal variable and if the pid file creates successfully
        //then we assign it. This is because the stop method uses this to delete
        //the file and is not convenient to assing it until it is valid.
        File tempFile = getPidFileForCreation();

		if (!createPidFile(tempFile)) {
			stop();
			return;
		}

		if (!pidFileHandler.isValid()) {
			logger.severe("Existing pid file found, " +
                    "this service is already running.");
			stop();
			return;
		}

        //Our turn to check if we will detach and enable file logging
        if (isDaemon()) {
            //Closing standard output, input and error, any message that
            //goes to them will be now discarded
            detach();
        }

        logger.finest("OsService " + getName() + " running with pid " +
                getPid() + (isDaemon() ? " as daemon." : "."));
        logger.finest("Java version: " + System.getProperty("java.version") +
                ", Vendor: " + System.getProperty("java.vendor") +
                ", OS: " + System.getProperty("os.name") +
                ", ARCH: " + System.getProperty("os.arch"));
        logger.finest(
                "VM version: " + System.getProperty("java.vm.version") +
                ", VM vendor: " + System.getProperty("java.vm.vendor") +
                ", VM name: " + System.getProperty("java.vm.name"));
        logger.finest("JAVAHOME: " + System.getProperty("java.home"));
        logger.finest("CLASSPATH: " + System.getProperty("java.class.path"));
        logger.finest("LIBPATH: " + System.getProperty("java.library.path"));
        logger.finest("PIDFILE: " + getPidFile().getAbsolutePath());
		
        if (logFile != null) {
            logger.finest("LOGFILE: " + logFile.getAbsolutePath());
        } else {
            logger.finest("Logging to console.");
        }
    }

    /**
     * Creates the abstract path for the log file using the name of the service
     * and the extension '.log'.
     *
     * First the settings (if any) are searched for Keys.LOGFILE to find the
     * log path, if no settings or no logPath are found the path is got from
     * OsEnvironment.getLogPath().
     * 
     * @return true if the log was successfully set or false if not
     */
    protected File getLogFileForCreation() {
        File resultLogFile = null;

        String logPath = settings.get(
                Keys.LOGFILE.value,
                null);

        if (logPath != null) {
            resultLogFile = new File(logPath);
        } else {
            resultLogFile = new File(
                    OsEnvironment.get().getLogPath(), getName() + ".log");
        }

        return resultLogFile;
    }

    /**
     * Redirects all the logging for this service to a log file. This also
     * disables the default logging method.
     * 
     * @param logFile File to use for logging.
     * @param logSize Maximum size of the file in bytes before rotating.
     * @param logNum Number of log files to keep.
     * @return the file handler on success or null if fails
     * @throws IllegalStateException If there is no logger set or if the
     * logSize parameter is equal or less than 0.
     * @throws NullPointerException If the log file
     */
    protected FileHandler createLogFileHandler(File logFile, int logSize,
            int logNum) {

        if (logger == null) {
            throw new IllegalStateException("Logging not initiallized");
        }

        if (logSize <= 0) {
            throw new IllegalArgumentException(
                    "File size can't be zero or less");
        }

        if (logNum <= 0) {
            logNum = 1;
        }

        if (logFile == null) {
            throw new NullPointerException("File is null");
        }

        if (!OsEnvironment.get().ensurePath(logFile.getParentFile())) {
            logger.severe("Can't write to log file folder " +
                    logFile.getParentFile().getPath());
            return null;
        }

        //Before detach create the log
        FileHandler handler = null;

        try {
            handler = new FileHandler(
                    logFile.getPath(),
                    logSize,
                    logNum,
                    true);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Can't create log file: " + ex.getMessage(), ex);
        } catch (SecurityException ex) {
            logger.log(Level.SEVERE, "Can't create log file: " + ex.getMessage(), ex);
        }

        return handler;
    }

    /**
     * Configures the default logging. This method is the first thing called
     * at setup to set the level specified in the configuration for the default
     * logger and to initialize the logger from Appplication static property.
     */
    protected void configureLogging() {
        Level logLevel = null;
        LogHandlerType type = null;
        Handler handler = null;

        //If there is a logger no configuration is done.

        if (logger != null) {
            return;
        }

        //We don't have a logger. we must create one
        logger = Logger.getLogger(getName());

        //Now we must configure our logger but first we must get some
        //configuration variables.

        try {
            logLevel = Level.parse(
                    settings.get(Keys.LOGLEVEL.value, "INFO").toUpperCase());
        } catch (IllegalArgumentException ex) {
            logLevel = Level.INFO;
        }

        logger.setLevel(logLevel);

        try {
            type = LogHandlerType.valueOf(
                    settings.get(Keys.LOGHANDLER.value, "DEFAULT").toUpperCase());
        } catch (IllegalArgumentException ex) {
            type = LogHandlerType.DEFAULT;
        }

        //Create the handler
        switch (type) {
            case CONSOLE:
                handler = new ConsoleHandler();
                logger.setUseParentHandlers(false);
                break;
            case FILE:
                //Defaults to files of 750K.
                int logSize = settings.getInt(Keys.LOGSIZE.value, 768000);
                int logNum = settings.getInt(Keys.LOGNUMBER.value, 4);
                logFile = getLogFileForCreation();

                handler = createLogFileHandler(logFile, logSize, logNum);

                if (handler == null) {
                    throw new IllegalArgumentException(
                            "Can't create log file handler for file " +
                            logFile.getAbsolutePath());
                } else {
                    System.out.println(
                            "Using log file: " + logFile.getAbsolutePath());
                }

                logger.setUseParentHandlers(false);
                break;
            case DEFAULT:
                logger.setUseParentHandlers(true);
                break;
            default:
                throw new IllegalArgumentException(
                        "Logging method " + type + " not supported.");
        }

        if (handler != null) {
            handler.setLevel(Level.ALL);
            handler.setFormatter(new SimplifiedFormater(true));
            logger.addHandler(handler);
        }
    }

    /**
     * Gets the pid file's full path for this service. This method first tries
     * to load that from configuration and then tries to use
     * OsEnvironment.getPidPath and use the service name as file name.
     * 
     * @return the pid file's full path or null if not able to get it.
     */
    protected File getPidFileForCreation() {

        File resultPidFile = null;

        String pidPath = settings.get(
                Keys.PIDFILE.value,
                null);

        if (pidPath != null) {
            resultPidFile = new File(pidPath);
        } else {
            resultPidFile = new File(
                    OsEnvironment.get().getPidPath(), getName() + ".pid");
        }

        return resultPidFile;
    }

    /**
     * Physically creates a pid file and writes into it the pid of this process.
     * The contents of the file are the pid process and a line feed character.
     * @param pidFile Full file path with extension
     * @return false if any exception was thrown or true if not.
     */
    protected boolean createPidFile(File pidFile) {
		pidFileHandler = new PidFileHandler(pidFile, getPid());
		
		try {
			pidFileHandler.create();
		} catch (IOException ex) {
			if (logger != null) {
				logger.log(Level.SEVERE, "Can't create pid file", ex);
			}
			return false;
		}

		return true;
    }

    /**
     * Method called continuously after setup() and that stops only if stop()
     * is called
     */
    protected abstract void work();

    /**
     * Starts the controller work. This implementation does nothing.
     * @param controller
     */
    protected void runController(OsServiceController controller) {
    }

    @Override
    protected void onRunAborted(RuntimeException ex) {
        if (logger != null) {
            logger.log(Level.SEVERE, "Error ", ex);
        } else {
            ex.printStackTrace();
        }
    }
}
