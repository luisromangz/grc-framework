/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.os;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Performs some os-sensitive operations in an independent way.
 */
public abstract class OsEnvironment {

    static {
        //Detect environment
        osInfo = new OsInfo();
        OsEnvironment.osInfo.detect();
    }
    private static OsInfo osInfo;

    /**
     * @return the current OsInfo object instance
     */
    public static OsInfo getOsInfo() {
        return osInfo;
    }
    private static OsEnvironment instance = null;

    /**
     * @return an instance for the current os
     */
    public static OsEnvironment get() {
        if (instance == null) {
            switch (osInfo.getKind()) {
                case Linux:
                    instance = new LinuxEnvironment();
                    break;
                case Windows:
                    instance = new WindowsGenericEnvironment();
                    break;
                default:
                    throw new UnsupportedOsException();
            }
        }

        return instance;
    }

    /**
     * Gets a path to place a log file. If the user is a regular user puts the
     * file in its user folder, but if it is a system account puts the file in
     * a system's folder.
     * @return an abstract path to the folder where to put a log file
     */
    public abstract File getLogPath();

    /**
     * Gets a path to place a pid file. If the user is a regular user puts the
     * file in its user folder, but if it is a system account puts the file in
     * a system's folder.
     * @return a path to place a pid file
     */
    public abstract File getPidPath();

    /**
     * Gets the system's default temporal path
     * @return
     */
    public abstract String getTmpPath();

    /**
     * Gets a path to place a pid file. Puts the file in the current user folder
     * a system's folder.
     * @return a path to place a pid file
     */
    public abstract File getUserPidPath();

    /**
     * Gets a path to place a log file in current users's account.
     * @return a path to place a log file
     */
    public abstract File getUserLogPath();

    /**
     * Gets a path to place a log file under system's logging path
     * @return a path to place a log file
     */
    public abstract File getSystemLogPath();

    /**
     * Gets a path to place a pid file under system's run path
     * @return a path to place a pid file
     */
    public abstract File getSystemPidPath();

    /**
     * Ensures that a directory exists and is writable, it doesn't exists it
     * will try to create it
     * @param directory
     * @return true if the directory exists/was created and is writable, false
     * if not.
     */
    public boolean ensurePath(File directory) {
        if (directory == null) {
            return false;
        }

        if (directory.exists()) {
            return directory.canWrite();
        } else {
            return directory.mkdirs();
        }
    }

    /**
     * Creates a new file taking care of creating the path to it and removing
     * any existing file if the overwrite flag is set.
     * @param file
     * @param overwrite
     * @param append 
     * @return
     */
    public boolean createFile(File file, boolean overwrite, boolean append) {

        File directory = file.getParentFile();

        ensurePath(directory);

        try {

            if (file.exists()) {
                if (overwrite) {
                    file.delete();
                } else if (append && !file.canWrite()) {
                    return file.canWrite();
                }
            } else {
                return file.createNewFile();
            }

        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    /**
     * Checks if a host is reachable. <br/>The test is done using the os
     * ping command to send 5 icmp probes with a 2 second timeout for each
     * probe.
     * @param inetAddress
     * @return
     */
    public boolean checkHost(InetAddress inetAddress) {
        return checkHost(inetAddress, 5, 2);
    }

    /**
     * Checks if a host is reachable.<br/>
     *
     * This method doesn't rely on InetAddress:isReachable stuff and execs
     * directly the system's checkHost command to check. It's the only reliable way
     * of doing an icmp echo with java without permissions.<br/>
     *
     * @param inetAddress host address
     * @param numProbes number of echo icmp to send
     * @param timeout timeout for each icmp probe
     * @return true if it is reachable or not
     * @throws UnsupportedOperationException if the underlying os doesn't
     * supports the ping command operation.
     */
    public boolean checkHost(InetAddress inetAddress, int numProbes,
            int timeout) {
        if (inetAddress == null) {
            throw new IllegalArgumentException("The ip address is required.");
        }

        if (numProbes <= 0 || numProbes > 32) {
            throw new IllegalArgumentException("The number of ping probes " +
                    "must be between 1 and 32");
        }

        if (timeout < 1 || timeout > 5) {
            throw new IllegalArgumentException("The timeout must be between " +
                    "1 and 5 seconds.");
        }

        Process ping = null;
        boolean alive = false;
        Integer result = null;

        try {
            ping = getOsPingProcess(inetAddress, numProbes, timeout);

            if (ping == null) {
                throw new UnsupportedOperationException(
                        "Ping command not supported in the current os.");
            }
        } catch(UnsupportedOperationException ex) {
            throw ex;
        }

        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(ping.getErrorStream()));

        try {
            if (bufReader.ready()) {
                System.out.println("Reading output");
                String line = bufReader.readLine();

                while (line != null) {
                    System.out.println(line);
                    line = bufReader.readLine();
                }
            }
        } catch (Exception ex) {
        }

        bufReader = new BufferedReader(
                new InputStreamReader(ping.getInputStream()));


        try {
            if (bufReader.ready()) {
                System.out.println("Reading output");
                String line = bufReader.readLine();

                while (line != null) {
                    System.out.println(line);
                    line = bufReader.readLine();
                }
            }
        } catch (Exception ex) {
        }

        //Wait the necesary time to wait all the probes plus 2 second
        result = waitProcess(ping, (numProbes * timeout + 1) * 2000);
        alive = result != null && ((int) result) == 0;

        return alive;
    }

    /**
     * Creates the concrete os command and returns it. If the os doesn't
     * supports this it must return null;
     * @param inetAddress
     * @param numProbes
     * @param timeout
     * @return a Process ready to execute with the concrete command to ping.
     */
    protected abstract Process getOsPingProcess(InetAddress inetAddress,
            int numProbes, int timeout);

    /**
     * Runs a process and waits for a limited time for it to finish. If the
     * process finishes before the timeout time this method returns the integer
     * exit code. If not this method returns a null.
     * @param process Process to wait
     * @param timeout milliseconds to wail before killing the process
     * @return the exist code of the process or null.
     */
    protected Integer waitProcess(Process process, long timeout) {
        Integer result = null;
        KillProcessTask task = new KillProcessTask(process);
        task.setInterval(timeout);
        task.setStartDelay(timeout);
        task.run();

        try {
            result = process.waitFor();
        } catch (InterruptedException ex) {
        } finally {
            task.stop();
        }

        return result;
    }
}
