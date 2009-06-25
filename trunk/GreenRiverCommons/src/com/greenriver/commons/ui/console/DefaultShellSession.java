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
 * Default implementation for the session interface (ShellSession).
 */
public class DefaultShellSession implements ShellSession {

    private Map<String, Object> properties;
    private Map<String, Boolean> flags;
    private Map<ShellCommand, ShellCommandData> data;
    private CommandHistory history;
    private boolean exit;

    public DefaultShellSession() {
        properties = new HashMap<String, Object>();
        flags = new HashMap<String, Boolean>();
        history = new DefaultCommandHistory(128);
    }

    public Object getProperty(String name) {
        if (properties.containsKey(name)) {
            return properties.get(name);
        } else {
            return null;
        }
    }

    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    public ShellCommandData data(ShellCommand command) {
        
        if (!data.containsKey(command)) {
            data.put(command, new DefaultShellCommandData());
        }

        return data.get(command);
    }

    public boolean isExit() {
        return exit;
    }

    public void setIsExit(boolean value) {
        exit = value;
    }

    public CommandHistory history() {
        return history;
    }

    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    public boolean hasFlag(String name) {
        return flags.containsKey(name);
    }

    public boolean getFlag(String name) {
        return flags.get(name);
    }

    public void setFlag(String name, boolean flagValue) {
        flags.put(name, flagValue);
    }
}
