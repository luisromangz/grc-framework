/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.net;

import java.net.SocketAddress;

/**
 * Address for serial sockets. Only holds the device name
 */
public class SerialSocketAddress extends SocketAddress {

    private String deviceName;

    /**
     * @return the deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    public SerialSocketAddress(String deviceName) {
        this.deviceName = deviceName;
    }
}
