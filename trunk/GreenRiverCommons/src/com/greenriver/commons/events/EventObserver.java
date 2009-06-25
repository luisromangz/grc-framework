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
 * Contract for Observable (from observer pattern) implementations.
 * <br/><br/>
 * We only need to define the base notify method that will be used by observables
 * to send notifications to implementations of this interface.
 *
 * @param <T> Type of the arguments for this Observer
 */
public interface EventObserver<T> {

    /**
     * Notifies about a change in the observable pushing the relevant data as
     * a strong-typed argument.
     *
     * @param sender source of the notify
     * @param args relevant status data that have changed
     */
    public void notify(Object sender, T args);
}
