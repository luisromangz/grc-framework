/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.ui.console;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the interface as simply a map wrapper.
 */
public class DefaultShellCommandData implements ShellCommandData {

    private Map<String, Object> map;

    public DefaultShellCommandData() {
        map = new HashMap<String, Object>();
    }

    public Object get(String name) {
        return map.get(name);
    }

    public void set(String name, Object value) {
        map.put(name, value);
    }
    
}
