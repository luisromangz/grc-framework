/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.os;

/**
 * Common service operations
 * @author mangelp
 */
public enum RunControl {
    /**
     * Soft stop
     */
    STOP,
    /**
     * Soft restart
     */
    RESTART,
    /**
     * Hard restart
     */
    ABORT,
    /**
     * Get status data
     */
    STATUS
}
