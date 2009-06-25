/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.net;

import java.util.Random;

/**
 * Utility class to generate port numbers and do some checks about their ranges.
 */
public class PortNumber {

    //First non reserved port
    private static final int reservedPortLimit = 1024;
    //First non client port
    private static final int clientPortLimit = 4095;
    //First non server port (reserved chunk for dynamic)
    private static final int serverFixedPortLimit = 49152;
    //Port value out of range (16-bit port encoding)
    private static final int serverDynamicPortLimit = 65536;
    //Randomizer
    private static final Random rand = new Random();

    /**
     * Checks that the port is in the range 1025 - 4095
     *
     * @param portNumber
     * @return if it is valid or not
     */
    public static boolean isValidForClient(int portNumber) {
        return reservedPortLimit <= portNumber &&
                portNumber < clientPortLimit;
    }

    /**
     * Checks that the port is in the range 1025 - 49152
     * 
     * @param portNumber
     * @return if it is valid or not
     */
    public static boolean isValidForServer(int portNumber) {
        return reservedPortLimit <= portNumber &&
                portNumber < serverFixedPortLimit;
    }

    /**
     * Checks that the port is in the range 49152 - 65534
     *
     * @param portNumber
     * @return if it is valid or not
     */
    public static boolean isValidForDynamic(int portNumber) {
        return serverFixedPortLimit <= portNumber &&
                portNumber < serverDynamicPortLimit;
    }

    /**
     * Gets a random port for a client socket
     * @return a port number in the range 1025 - 4095
     */
    public static int getClientPortNumber() {
        return rand.nextInt(clientPortLimit - reservedPortLimit - 1)
                + reservedPortLimit;
    }

    /**
     * Gets a random port for a server socket
     * @return a port number in the range 1025 - 49152
     */
    public static int getServerPortNumber() {
        return rand.nextInt(serverFixedPortLimit - reservedPortLimit - 1)
                + reservedPortLimit;
    }

    /**
     * Gets a random port for a dynamic allocation
     * @return a port number in the range 49152 - 65534
     */
    public static int getDynamicPortNumber() {
        return rand.nextInt(serverDynamicPortLimit - serverFixedPortLimit - 1)
                + serverFixedPortLimit;
    }
}
