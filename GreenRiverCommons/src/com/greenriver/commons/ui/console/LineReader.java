/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.ui.console;

import java.io.IOException;

/**
 * Line reader operations
 * @author mangelp
 */
public interface LineReader {

    /**
     * Reads a line from input
     * @return the line read
     * @throws java.io.IOException in the input stream fails in reading
     */
    public String readLine() throws IOException;
}
