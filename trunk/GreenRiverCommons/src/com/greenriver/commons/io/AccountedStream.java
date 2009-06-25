/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenriverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.com/licensing/greenriver-license

Author: mangelp
###################################################################*/

package com.greenriver.commons.io;

/**
 *
 * @author mangelp
 */
public interface AccountedStream {
    /**
     * Return the count of bytes read or write by the stream.
     * @return the number of bytes read or write from or to an stream.
     */
    long getByteCount();
}
