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

/**
 * Operating system information
 */
public class OsInfo {

    private OsKind kind;
    private String version;
    private String distro;
    private MachineArch arch;

    /**
     * @return the os type
     */
    public OsKind getKind() {
        return kind;
    }

    /**
     * @param kind OS type
     */
    public void setKind(OsKind kind) {
        this.kind = kind;
    }

    /**
     * @return the version of the OS
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version OS version
     */
    void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the distro name
     */
    public String getDistro() {
        return distro;
    }

    /**
     * @param distro name
     */
    void setDistro(String distro) {
        this.distro = distro;
    }

    /**
     * @return the architecture
     */
    public MachineArch getArch() {
        return arch;
    }

    /**
     * @param arch machine architecture
     */
    void setArch(MachineArch arch) {
        this.arch = arch;
    }

    /**
     * Default constructor
     */
    public OsInfo() {
    }

    /**
     * Detection of the variables and creation of the right enumerations
     */
    void detect() {

        String data = System.getProperty("os.name").toLowerCase();

        distro = "Unknown";
        
        if (data.indexOf("linux") >= 0) {
            kind = OsKind.Linux;
            
            File test = new File("/etc/fedora-release");

            if (test.exists()) {
                distro = "fedora";
            }

            test = new File("/etc/debian-release");

            if (test.exists()) {
                distro = "debian";
            }

            test = new File("/etc/ubuntu-release");

            if (test.exists()) {
                distro = "ubuntu";
            }
        } else if (data.indexOf("win") >= 0 || data.indexOf("Win") >= 0) {
            kind = OsKind.Windows;
        } else {
            kind = OsKind.Unknown;
        }

        version = System.getProperty("os.version").toLowerCase();

        data = System.getProperty("os.arch").toLowerCase();

        if (data.indexOf("64") >= 0) {
            arch = MachineArch.IA64;
        } else if (data.indexOf("32") >= 0) {
            arch = MachineArch.IA32;
        } else {
            arch = MachineArch.Unknown;
        }
    }
}
