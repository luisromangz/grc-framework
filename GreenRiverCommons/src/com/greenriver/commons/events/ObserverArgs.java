/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.events;

/**
 * Generic observer argument class with support for notify cancelation. Also an
 * empty instance is defined for convenience use as a default argument.
 */
public class ObserverArgs {

    /**
     * This is the empty instance used as a placeholder when an instance is
     * required instead of a null reference.
     * We use a singleton to avoid wasting memory with more than one instance.
     */
    private static final ObserverArgs empty = new ObserverArgs();

    /**
     * @return the empty instance
     */
    public static ObserverArgs empty() {
        return ObserverArgs.empty;
    }

    /**
     * Checks if the instance is the empty one
     * @param args
     * @return true if the instance is the empty one or false if not
     */
    public static boolean isEmpty(ObserverArgs args) {
        return args == ObserverArgs.empty;
    }

    private boolean cancelled;

    /**
     * Checks if the notify is cancelled
     * @return true if it is cancelled or false if not
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the flag to cancel the notify bubbling
     * @param value enables or disables notify for other observers
     */
    public void setCancelled(boolean value) {
        cancelled = value;
    }

    /**
     * Default constructor
     */
    public ObserverArgs() {
    }
}
