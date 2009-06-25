/*#################################################################
(c)2008/2009 Greenriver Consulting SL. All rights reserved.

This project and all subsequent files are licensed under the terms
and conditions of the GreenRiverLicense.

If you haven't received a copy of the license you can obtain it at
http://www.greenriverconsulting.es/licensing/greenriverlicense

Author: mangelp
###################################################################*/

package com.greenriver.commons.tasks;

import com.greenriver.commons.events.ObserverArgs;

/**
 *
 */
public class ExecutionFinishedObserverArgs extends ObserverArgs {

    private SchedulerTask task;

    public SchedulerTask getTask() {
        return task;
    }

    public ExecutionFinishedObserverArgs() {
        super();
    }

    public ExecutionFinishedObserverArgs(SchedulerTask task) {
        super();
        this.task = task;
    }

}
