/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.os;

import java.io.Serializable;

/**
 * Models a command to be sent
 */
public abstract class OsServiceCommand implements Serializable {
    /**
     * The command is a request if this flag is true. If false is a response.
     */
    private boolean request;
    /**
     * The command is an acknowledge to the other end.
     */
    private boolean ack;

    /**
     * Checks if this command is a request command
     * @return the request
     */
    public boolean isRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(boolean request) {
        this.request = request;
    }

    /**
     * Checks if this is a acknowledgement command
     * @return the ack
     */
    public boolean isAck() {
        return ack;
    }

    /**
     * @param ack the ack to set
     */
    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public OsServiceCommand() {
    }

    public OsServiceCommand(boolean request, boolean ack) {
        this.request = request;
        this.ack = ack;
    }

}
