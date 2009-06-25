/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenriverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.com/licensing/greenriver-license

Author: mangelp
###################################################################*/

package com.greenriver.commons.log;

/**
 * Types of handlers for logging
 * @author mangelp
 */
public enum LogHandlerType {
    /**
     * File handler.
     */
    FILE,
    /**
     * Console handler.
     */
    CONSOLE,
    /**
     * Use the default handler.
     */
    DEFAULT
}
