/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/
package com.greenriver.commons.events;

import java.util.ArrayList;

/**
 * Default implementation of an EventObservable, an Observable of the Observer
 * design pattern.<br/><br/>
 *
 * The implementation is pretty simple, it handles the collection of observers,
 * ensures that the arguments extend from ObserverArgs and iterates the registered
 * observers.<br/><br/>
 *
 * The use of ObseverArgs as the base class for arguments is a convenience
 * decision for this concrete implementation as it uses the cancel flag to stop
 * notification loop.
 * 
 * @param <T> Type of the class to use for arguments
 */
public class DefaultEventObservable<T extends ObserverArgs>
        implements EventObservable<T> {

    /**
     * Strong-typed array of observers
     */
    private ArrayList<EventObserver<T>> observers;

    public boolean isEmpty() {
        return observers.isEmpty();
    }

    /**
     * Default constructor.
     */
    public DefaultEventObservable() {
        observers = new ArrayList<EventObserver<T>>(12);
    }

    /**
     * Adds a new observer
     * <br/><br/>
     * If the observer already exists this method does nothing.
     *
     * @param observer to register in this obsevable
     */
    public void addObserver(EventObserver<T> observer) {
        if (observers.contains(observer)) {
            return;
        }

        observers.add(observer);
    }

    /**
     * Removes an existing observer.
     * <br/><br/>
     * If the observer have been already removed this method does nothing.
     * 
     * @param observer
     */
    public void removeObserver(EventObserver<T> observer) {
        int pos = observers.indexOf(observer);

        if (pos < 0) {
            return;
        }

        observers.remove(pos);
    }

    /**
     * Equivalent call to notifyObservers(this, null)
     */
    public void notifyObservers() {
        notifyObservers(this, null);
    }

    /**
     * Equivalent call to notifyObservers(this, args)
     * @param args data updated
     */
    public void notifyObservers(T args) {
        notifyObservers(this, args);
    }

    /**
     * Notifies each observer about the state change providing the data
     * <br/><br/>
     * This method supports the args.isCancelled() flag. So if an observer
     * turns on this flag the iteration loop stops and no more notifies are
     * sent.
     *
     * @param obj sender of the notify
     * @param args data updated
     */
    public void notifyObservers(Object obj, T args) {
        for (EventObserver<T> iObserver : observers) {
            iObserver.notify(this, args);
            if (args.isCancelled()) {
                break;
            }
        }
    }
}
