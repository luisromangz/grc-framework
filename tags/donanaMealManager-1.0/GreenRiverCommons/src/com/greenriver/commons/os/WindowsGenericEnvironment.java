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
import java.net.InetAddress;

/**
 * Concrete implementation for windows. Basically puts in some custom paths and
 * command arguments.
 */
public class WindowsGenericEnvironment extends OsEnvironment {

    //Don't work, we can't use it.
//    private static final String pingCmd =
//            "ping.exe -n numProbes -w timeout";

    protected boolean maybeIsAdmin(String userName) {
        return userName.indexOf("admin") >= 0 ||
                userName.indexOf("Admin") >= 0;
    }

    @Override
    public File getLogPath() {
        //This is a bad test but is the only thing we can do
        String userName = System.getProperty("user.name", null);
        if (userName == null || maybeIsAdmin(userName)) {
            return getSystemLogPath();
        } else {
            return getUserLogPath();
        }
    }

    @Override
    public File getPidPath() {
        //This is a bad test but is the only thing we can do
        String userName = System.getProperty("user.name", null);
        if (userName == null || maybeIsAdmin(userName)) {
            return getSystemPidPath();
        } else {
            return getUserPidPath();
        }
    }

    @Override
    public File getUserPidPath() {
        String userHome = System.getProperty("user.home", null);

        if (userHome == null) {
            userHome = System.getProperty("java.io.tmpdir");
        }

        return new File(userHome, "greenriverc\\pid");
    }

    @Override
    public File getUserLogPath() {
        String userHome = System.getProperty("user.home", null);

        if (userHome == null) {
            userHome = System.getProperty("java.io.tmpdir");
        }

        return new File(userHome, "greenriverc\\log");
    }

    @Override
    public File getSystemLogPath() {
        File file = new File(System.getenv("windir"));
        file = file.getParentFile();
        return new File(file.getAbsolutePath(), "greenriverc\\log");
    }

    @Override
    public File getSystemPidPath() {
        File file = new File(System.getenv("windir"));
        file = file.getParentFile();
        return new File(file.getAbsolutePath(), "greenriverc\\pid");
    }

    @Override
    public Process getOsPingProcess(InetAddress inetAddress,
            int numProbes, int timeout) {
        //Calling ping from java always return 1 as the result, that cannot
        //be used to do the check so we can't use it.
        return null;
//        Process ping = null;
//        String cmdPath = System.getenv("WINDIR")+"\\system32\\";
//
//        //Convert timeout to millis as it that what windows checkHost command
//        //uses.
//        timeout = timeout * 1000;
//
//        String cmd = cmdPath +
//                pingCmd.replaceFirst("numProbes", "" + numProbes).
//                replaceFirst("timeout", "" + timeout) + " " +
//                inetAddress.getHostAddress();
//
//        System.out.println("Executing command " + cmd);
//
//        try {
//            ping = Runtime.getRuntime().exec(cmd);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        return ping;
    }

    @Override
    public String getTmpPath() {
        return System.getProperty("java.io.tmpdir");
    }
}
