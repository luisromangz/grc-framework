/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.os;

/**
 * Service controller process. Does communication with another service controller
 * and allows to send commands remotelly to the service and give back the responses.
 *
 * This controller implementation runs as long as the service does. When the
 * service finishes the stop method is called from the service. If a command is
 * issued to end the service the handler for the command must call the stop
 * method on the service as this will call stop on the controller.
 *
 * The infinite loop must be done meanwhile the flag isStopRunning is false.
 * The inheritor must do that in any way he wants to.
 */
public abstract class OsServiceController {

    private OsService service;
    private Thread thread;
    private boolean stopRunning;

    public boolean isStopRunning() {
        return stopRunning;
    }
    
    /**
     * Returns a reference to the thread where this service is executing
     * @return
     */
    public Thread getThread() {
        return thread;
    }

    public OsService getService() {
        return service;
    }

    public OsServiceController(OsService service) {
    }

    /**
     * Sends a command and waits for the response
     * @param cmd
     * @param destination 
     * @return
     */
    protected abstract Object send(OsServiceCommand cmd, Object destination);

    /**
     * Listens for input commands
     * @return
     */
    protected abstract OsServiceCommand receive();

    /**
     * Runs the controller. This call blocks until the controller finishes, so
     * it should be run in another thread
     */
    protected abstract void runInternal();

    /**
     * Runs the controller
     */
    public final void run() {
        thread = Thread.currentThread();
        runInternal();
    }

    /**
     * Stops the procesing making the run method to end. 
     * 
     * This should not call stop in the service because that method might also
     * call this one and start an infinite loop.
     *
     * If you want to stop both threads do the stop call only for the service.
     */
    public void stop() {
        stopRunning = true;
    }
}
