/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.ui.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Implementation that reads input directly from System.in
 */
public class DefaultLineReader implements LineReader {

    private BufferedReader reader;

    public DefaultLineReader() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

}
