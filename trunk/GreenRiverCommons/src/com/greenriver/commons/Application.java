/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons;

import com.greenriver.commons.configuration.DictionarySettingsStorage;
import com.greenriver.commons.configuration.SettingsProvider;
import com.greenriver.commons.configuration.SettingsStorage;
import com.greenriver.commons.configuration.ApplicationSettingsProvider;
import com.greenriver.commons.os.OsEnvironment;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Central application facilities. Has the general log to be used by all parts
 * and the os environment plus extra functionality like managing a set of
 * objects that handle this shutdown process or access to global configuration
 * settings.
 */
public class Application {

    /**
     * Handles Mv shutdown to do a call to the shutdown method.
     */
    private class MVShutdownHandler extends Thread {

        public MVShutdownHandler() {
        }

        @Override
        public void run() {
            shutdown();
        }
    }
    private static Application instance = null;
    private static boolean initialized;
    private static Pattern pidMatchPattern;

    static {
        instance = new Application();
        //Keep this cached
        pidMatchPattern = Pattern.compile("^([0-9]+)@.+$",
                Pattern.CASE_INSENSITIVE);
    }

    /**
     * Checks if the initialization have been done.
     * @return if the initialization have been done.
     */
    public synchronized static boolean isInitialized() {
        return initialized;
    }

    /**
     * Gets if the Application have been already configured.
     * @return
     */
    public synchronized static boolean isConfigured() {
        return instance.settings != null;
    }

    /**
     * Gets the settings in use by the application
     * @return settings or null.
     */
    public synchronized static SettingsProvider settings() {
        return instance.settings;
    }

    /**
     * Gets the current instance.
     * @return The current instance
     */
    public synchronized static Application get() {
        return instance;
    }

    public synchronized static void setup(Object initializer) {
        setup(initializer, Logger.getLogger("ApplicationDefaultLogger"));
        instance.logger.setLevel(Level.INFO);
        instance.logger.setUseParentHandlers(true);
    }

    /**
     * Configures the global logger
     * @param logger Logger instance to use
     * @param initializer Object that is doing the initialization
     */
    public synchronized static void setup(Object initializer, Logger logger) {
        setup(initializer, initializer.getClass().getSimpleName(), null,
                logger);
    }

    /**
     * Configures the application name and the global logger
     * @param initializer Object that is doing the initialization
     * @param name Application name
     * @param logger Logger instance to use
     */
    public synchronized static void setup(Object initializer, String name,
            Logger logger) {
        setup(initializer, name, null, logger);
    }

    /**
     * Configures the application name and the global logger
     * @param initiallizer Object that is doing the initialization
     * @param name Application name
     * @param description Application description
     * @param logger Logger instance to use
     */
    public synchronized static void setup(Object initiallizer, String name,
            String description, Logger logger) {

        if (initialized) {
            throw new IllegalStateException("Application setup already done.");
        }

        if (initiallizer == null || logger == null ||
                Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Null or empty argument.");
        }

        instance.initializer = initiallizer;
        instance.logger = logger;
        instance.appName = name;
        instance.description = description;
        initialized = true;
    }

    /**
     * Sets the configuration source provider for this application. This can
     * be done only after the setup have been done and the application have a
     * initiallizer.
     * @param initiallizer Object that is doing the initialization
     * @param settings Settings provider to use.
     * @throws IllegalStateException if the initiallizer is not the same that
     * did the setup
     */
    public synchronized static void configure(
            Object initiallizer,
            SettingsProvider settings) {
        if (initiallizer == null) {
            throw new NullPointerException("Initiallizer object is null.");
        }

        if (initiallizer != instance.initializer) {
            throw new IllegalStateException(
                    "The setup have been initiallized yet by another object.");
        }

        if (!Strings.equals(settings.getRoot(), instance.settingsRoot)) {
            throw new IllegalArgumentException(
                    "Settings provider must be configured with 'app' as the" +
                    " keys root.");
        }

        instance.settings = settings;
    }

    /**
     * Registers a manager that will be notified when the mv tries to shut
     * down.
     * @param manager ShutdownManager implementation to use.
     */
    public static synchronized void registerShutdownManager(
            ShutdownManager manager) {
        if (manager == null) {
            throw new NullPointerException("Null manager reference");
        }
        instance.managers.add(manager);
    }

    /**
     * Removes a previously registered manager.
     * @param manager ShutdownManager implementation to remove.
     */
    public static synchronized void unregisterShutdownManager(
            ShutdownManager manager) {
        if (manager == null) {
            throw new NullPointerException("Null manager reference");
        }
        instance.managers.remove(manager);
    }

    /**
     * Gets the current instance of OsEnvironment utility implementation.
     * @return the current instance of OsEnvironment.
     */
    public synchronized static OsEnvironment osEnvironment() {
        return instance.osEnvironment;
    }

    /**
     * Parses the jvm name of a process into its pid, if it is part of it.
     * <br/><br/>
     * Got from:<br/>
     * http://golesny.de/wiki/code:javahowtogetpid
     * <br/><br/>
     * tested on:
     * <ul>
     * <li>windows xp sp 2, java 1.5.0_13</li>
     * <li>mac os x 10.4.10, java 1.5.0</li>
     * <li>debian linux, java 1.5.0_13</li>
     * <li>windows server 2003, java 1.5</li>
     * </ul><br/>
     * all return pid@host, e.g 2204@antonius
     *
     * @param processName Name of the process to parse
     * @return the pid if it was part of the process name or -1 if failed to
     * parse it.
     */
    private static Integer tryParsePid(String processName) {
        int result = -1;

        Matcher matcher = pidMatchPattern.matcher(processName);

        if (matcher.matches()) {
            try {
                result = Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException ex) {
                result = -1;
            }
        }

        return result;
    }
    private Logger logger;
    private String appName;
    private String description;
    private OsEnvironment osEnvironment;
    private List<ShutdownManager> managers;
    private SettingsProvider settings;
    private boolean shuttingDown = false;
    private final Object shutdownLock = new Object();
    private Object initializer;
    private String settingsRoot = "app";

    /**
     * Private constructor to avoid accidental instantiation.
     */
    private Application() {
        osEnvironment = OsEnvironment.get();
        managers = new ArrayList<ShutdownManager>(25);
        Runtime.getRuntime().addShutdownHook(new MVShutdownHandler());
        logger = Logger.getLogger(getClass().getSimpleName());
        logger.setLevel(Level.INFO);

        SettingsStorage storage = new DictionarySettingsStorage();
        settings = new ApplicationSettingsProvider(storage, settingsRoot);
    }

    /**
     * Gets the logger. If a logger haven't been set it uses a default one
     * configured with INFO level and using the default parent logging.
     * @return a logger.
     */
    public synchronized Logger getLogger() {
        return logger;
    }

    /**
     * Returns the settings provider in use. If a settings provider haven't
     * been set it uses a default empty one.
     * @return a settings provider.
     */
    public synchronized SettingsProvider getSettings() {
        return settings;
    }

    /**
     * Gets the name of the application if it was configured or null otherwise.
     * @return application name or null.
     */
    public String getAppName() {
        return appName;
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public Object getInitializer() {
        return initializer;
    }

    /**
     * Tries to return the pid of the process.
     * This code is not safe between jvm implementations but is a temporal
     * solution while sun thinks another year or two about implementing this.
     * <br/><br/>
     * <b>References:</b><br/>
     * http://golesny.de/wiki/code:javahowtogetpid
     *
     * @return the pid of the current process on success, -1 on fail
     */
    public int getPid() {
        RuntimeMXBean rtb = ManagementFactory.getRuntimeMXBean();
        String processName = rtb.getName();
        return tryParsePid(processName);
    }

    /**
     * configuration root for settings
     * @return the configuration root key.
     */
    public String getSettingsRoot() {
        return settingsRoot;
    }

    /**
     * Calls all the run method of all ShutdownManagers registered and after
     * that frees all the resources used by this instance.
     */
    public void shutdown() {
        if (shuttingDown) {
            return;
        }

        synchronized (shutdownLock) {
            if (shuttingDown) {
                return;
            }
            shuttingDown = true;

            for (ShutdownManager manager : managers) {
                try {
                    manager.run();
                } catch (Throwable t) {
                    //Ignore it
                }
            }

            //Avoid shutdown stuff to get called again.
            managers.clear();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        //Alternatively if the mv registration doesn't happens the garbage
        //collector will make this call happen.
        shutdown();
    }
}
