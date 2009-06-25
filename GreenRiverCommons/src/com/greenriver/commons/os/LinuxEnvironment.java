/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.os;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Concrete implementation of the environment for a linux-based system.
 * Basically it uses some concrete command arguments and paths.
 */
public class LinuxEnvironment extends OsEnvironment {

    /**
     * Default paths
     */
    private static final String pingCmd = "ping -c numProbes -W timeout";

    @Override
    public Process getOsPingProcess(InetAddress inetAddress, int numProbes,
            int timeout) {
        Process ping = null;

        String cmd = pingCmd.replaceFirst("numProbes", "" + numProbes).
                replaceFirst("timeout", "" + timeout) +
                " " +
                inetAddress.getHostAddress();

        try {
            ping = Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            //Nothing
        }

        return ping;
    }

    @Override
    public File getUserPidPath() {
        String userHome = System.getProperty("user.home", null);

        if (userHome == null) {
            throw new IllegalStateException("No user home folder found!");
        }

        return new File(userHome, ".greenriverc/pid");
    }

    @Override
    public File getUserLogPath() {
        String userHome = System.getProperty("user.home", null);

        if (userHome == null) {
            throw new IllegalStateException("No user home folder found!");
        }

        return new File(userHome, ".greenriverc/log");
    }

    @Override
    public File getSystemLogPath() {
        return new File("/var/log");
    }

    @Override
    public File getSystemPidPath() {
        return new File("/var/run");
    }

    public File getLogPath() {
        String userName = System.getProperty("user.name", null);
        if (userName == null) {
            throw new IllegalStateException("No user name!");
        }
        if (userName.equals("root")) {
            return getSystemLogPath();
        } else {
            return getUserLogPath();
        }
    }

    public File getPidPath() {
        String userName = System.getProperty("user.name", null);
        if (userName == null) {
            throw new IllegalStateException("No user name!");
        }
        if (userName.equals("root")) {
            return getSystemPidPath();
        } else {
            return getUserPidPath();
        }
    }

    @Override
    public String getTmpPath() {
        return "/tmp";
    }
}
