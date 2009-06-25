/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

import com.greenriver.commons.events.DefaultEventObservable;

/**
 * Observable to notify the end of an execution cycle by a task
 */
public class ExecutionFinishedObservable
        extends DefaultEventObservable<ExecutionFinishedObserverArgs> {

    public ExecutionFinishedObservable() {
        super();
    }

}
