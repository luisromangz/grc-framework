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
 * Contract for those interested in implement the observer pattern with 
 * strong-typed arguments in the notification.<br/><br/>
 *
 * The idea of this interface is to define an observer where the arguments 
 * passed to observers during update iterations are the data that the observers
 * could be waiting of, that's the idea behind defining it as a generic type.
 * They also can directly do an access to the source of the notification and
 * check the status of it, but if you plan to do so you should be better using
 * java.util.Observer and java.util.Observable implementation of the pattern.
 * <br/><br/>
 * This interface also specifies an usefull overload of the notifyObservers
 * method that allows to pass the instance that will be used as the source of
 * the notification instead of the class that is implementing this contract.
 * This way we can provide implementations that act like delegates in charge of
 * storing client observer references and sending them the changed state data
 * (push model) along with a reference to the source of the data.
 * <br/><br/>
 * In this interface we have removed the burden of the internal status of the
 * observable as it can be used as a delegate with no status (well, only the
 * observer's list).
 * <br/><br/>
 * If you need some tips on generics check this out:
 * http://java.sun.com/j2se/1.5.0/docs/guide/language/generics.html
 *
 * @param <T> Type used for arguments
 * @author mangelp
 */
public interface EventObservable<T> {
    /**
     * Adds a new observer for the status update of the observer concrete data
     *
     * @param observer
     * @throws IllegalArgumentException If the observer was previously added
     */
    public void addObserver(EventObserver<T> observer);

    /**
     * Removes an existing observer
     *
     * @param observer
     * @throws IllegalArgumentException If the observer was not previously added
     */
    public void removeObserver(EventObserver<T> observer);

    /**
     * Iterates throught all registered observers and performs the status update
     * notification.
     * <br/><br/>
     * The sender of the notify is the same class that implements the interface
     */
    public void notifyObservers();

    /**
     * Iterates throught all registered observers and performs the status update
     * notification.
     * <br/><br/>
     * The sender of the notify is the same class that implements the interface
     * 
     * @param args status data to pass to observers
     */
    public void notifyObservers(T args);

    /**
     * Iterates throught all registered observers and performs the status update
     * notification.
     * 
     * @param obj sender of the notification
     * @param args status data to pass to observers
     */
    public void notifyObservers(Object obj, T args);
}