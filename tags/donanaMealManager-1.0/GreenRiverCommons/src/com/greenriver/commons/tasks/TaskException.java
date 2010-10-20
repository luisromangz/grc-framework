/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

/**
 * Exception to be thrown when a task can't continue nor handle the error. It's
 * a runtime exception so it don't force to be catched but you may pay attention
 * to do a cleanup in the task.
 */
public class TaskException extends RuntimeException {

    public TaskException() {
        super();
    }

    public TaskException(String msg, Throwable t) {
        super(msg, t);
    }

    public TaskException(String msg) {
        super(msg);
    }
}
