/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenriverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.com/licensing/greenriver-license

Author: mangelp
###################################################################*/

package com.greenriver.commons.configuration;

import java.io.File;

/**
 *
 * @author mangelp
 */
public interface FileSettingsSource {
    /**
     * Gets the path to the file as an abstract path object.
     * @return the abstract file path
     */
    File getFile();
}
